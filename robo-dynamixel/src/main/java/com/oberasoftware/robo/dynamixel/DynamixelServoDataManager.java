package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.ServoData;
import com.oberasoftware.robo.api.ServoDataManager;
import com.oberasoftware.robo.api.ServoProperty;
import com.oberasoftware.robo.api.ServoUpdateEvent;
import com.oberasoftware.robo.api.exceptions.RoboException;
import com.oberasoftware.robo.core.commands.ReadPositionAndSpeedCommand;
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

import static java.util.Collections.emptyMap;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelServoDataManager implements ServoDataManager, EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelServoDataManager.class);

    private ConcurrentMap<String, ServoDataHolder> servoDataMap = new ConcurrentHashMap<>();

    @Autowired
    private EventBus eventBus;

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
            LOG.debug("Servo: {} has has a value update for property: {} in last 50 ms. sending current data", servoId, property);
            return holder.getValue(property);
        } else {
            LOG.debug("Retrieving property: {} from servo: {}", property, servoId);
            if (property == ServoProperty.POSITION || property == ServoProperty.SPEED) {
                eventBus.publish(new ReadPositionAndSpeedCommand(servoId));
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
    public void receive(ServoUpdateEvent updateEvent) {
        String servoId = updateEvent.getServoId();
        ensureDataHolder(servoId);

        LOG.debug("Received an update event for servo: {}", servoId);
        ServoData servoData = updateEvent.getServoData();

        ServoDataHolder holder = servoDataMap.get(servoId);
        servoData.getKeys().forEach(d -> holder.putData(d, servoData.getValue(d)));

        LOG.debug("Signalling anyone waiting for the data");
        holder.signal();
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

        private void putData(ServoProperty property, Object value) {
            LOG.debug("Putting data in store: {}:{} for servo: {}", property, value, servoId);
            values.put(property, value);
            updateTimes.putIfAbsent(property, System.currentTimeMillis());
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
