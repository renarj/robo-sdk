package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.exceptions.RoboException;
import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoDataManager;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoDataReceivedEvent;
import com.oberasoftware.robo.api.servo.events.ServoUpdateEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.commands.ReadPositionAndSpeedCommand;
import com.oberasoftware.robo.core.commands.ReadTemperatureCommand;
import com.oberasoftware.robo.dynamixel.commands.DynamixelReadServoMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.abs;
import static java.util.Collections.emptyMap;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelServoDataManager implements ServoDataManager, EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelServoDataManager.class);

    private ConcurrentMap<String, ServoDataHolder> servoDataMap = new ConcurrentHashMap<>();

    @Autowired
    private LocalEventBus eventBus;

    @Override
    public Map<ServoProperty, Object> getCurrentValues(String servoId) {
        if(servoDataMap.containsKey(servoId)) {
            return servoDataMap.get(servoId).getValues();
        }

        return emptyMap();
    }

    @Override
    public <T> T readServoProperty(String servoId, ServoProperty property) {
        ensureDataHolder(servoId);
        ServoDataHolder holder = servoDataMap.get(servoId);

        if(holder.isUpdatedInTimeFrame(property, 200, TimeUnit.MILLISECONDS)) {
            LOG.info("Servo: {} has has a value update for property: {} in last 50 ms. sending current data", servoId, property);
            return holder.getValue(property);
        } else {
            LOG.debug("Retrieving property: {} from servo: {}", property, servoId);
            if (property == ServoProperty.POSITION || property == ServoProperty.SPEED) {
                eventBus.publish(new ReadPositionAndSpeedCommand(servoId));
            } else if (property == ServoProperty.TEMPERATURE) {
                eventBus.publish(new ReadTemperatureCommand(servoId));
            } else if (property == ServoProperty.MIN_ANGLE_LIMIT || property == ServoProperty.MAX_ANGLE_LIMIT) {
                eventBus.publish(new DynamixelReadServoMode(servoId));
            }

            LOG.debug("Waiting for servo data update");

            holder.waitForUpdate();

            LOG.debug("Got a signal of data update");

            return holder.getValue(property);
        }
    }

    @Override
    public boolean readServoProperties(String servoId, ServoProperty... properties) {
        return false;
    }

    private void ensureDataHolder(String servoId) {
        servoDataMap.putIfAbsent(servoId, new ServoDataHolder(servoId));
    }

    @EventSubscribe
    public ServoUpdateEvent receive(ServoDataReceivedEvent updateEvent) {
        String servoId = updateEvent.getServoId();
        ensureDataHolder(servoId);

        LOG.debug("Received an update event for servo: {}", servoId);
        ServoData servoData = updateEvent.getServoData();

        ServoDataHolder holder = servoDataMap.get(servoId);
        boolean changed = false;
        for(ServoProperty property : servoData.getKeys()) {
            if(holder.putData(property, servoData.getValue(property))) {
                changed = true;
            }
        }

        LOG.debug("Signalling anyone waiting for the data");
        holder.signal();

        if(changed) {
            return new ServoUpdateEvent(servoId, new ServoDataImpl(holder.getValues()));
        } else {
            return null;
        }
    }

    private class ServoDataHolder {
        private ConcurrentMap<ServoProperty, Object> values = new ConcurrentHashMap<>();
        private ConcurrentMap<ServoProperty, Long> updateTimes = new ConcurrentHashMap<>();

        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        private final String servoId;

        private ServoDataHolder(String servoId) {
            this.servoId = servoId;
        }

        private Map<ServoProperty, Object> getValues() {
            return new HashMap<>(values);
        }

        private <T> T getValue(ServoProperty property) {
            return (T)values.get(property);
        }

        private boolean isUpdatedInTimeFrame(ServoProperty property, long time, TimeUnit unit) {
            if(updateTimes.containsKey(property)) {
                long updateTime = updateTimes.get(property);
                long millisSinceUpdate = System.currentTimeMillis() - updateTime;

                return millisSinceUpdate < TimeUnit.MILLISECONDS.convert(time, unit);
            }

            return false;
        }

        private boolean putData(ServoProperty property, Object value) {
            LOG.debug("Putting data in store: {}:{} for servo: {}", property, value, servoId);

            boolean changed = false;
            Object v = values.get(property);

            // need to prevent servo drift ghost updates
            if(property == ServoProperty.POSITION) {
                if (v != null) {
                    //let's check for small minute servo drift, only if delta great then 1
                    int r = ((Integer) v) - ((Integer) value);
                    changed = abs(r) > 1;
                } else {
                    //initial value
                    changed = true;
                }
            } else if(v == null || !v.equals(value)) {
                changed = true;
            }

            values.put(property, value);
            updateTimes.putIfAbsent(property, System.currentTimeMillis());

            return changed;
        }

        private void waitForUpdate() {
            try {
                lock.lock();

                boolean found = condition.await(60, TimeUnit.SECONDS);
                if(!found) {
                    LOG.error("Did not get a servo update for servo: {}", servoId);
                    throw new RoboException("Could not read servo data for servo: " + servoId);
                }
            } catch (InterruptedException e) {
                LOG.error("", e);
            } finally {
                lock.unlock();
            }
        }

        private void signal() {
            lock.lock();
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
