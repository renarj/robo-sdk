package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.events.SensorEvent;
import com.oberasoftware.robo.api.sensors.ListenableSensor;
import com.oberasoftware.robo.api.sensors.SensorListener;
import com.oberasoftware.robo.api.sensors.SensorValue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractSensor<T extends SensorValue> implements ListenableSensor<T> {

    private List<SensorListener<T>> sensorListeners = new CopyOnWriteArrayList<>();

    @Override
    public void listen(SensorListener<T> listener) {
        this.sensorListeners.add(listener);
    }


    protected void notifyListeners(SensorEvent<T> event) {
        sensorListeners.forEach(l -> l.receive(event));
    }

}
