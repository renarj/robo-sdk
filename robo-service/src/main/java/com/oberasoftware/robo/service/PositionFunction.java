package com.oberasoftware.robo.service;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.annotations.EdmFunction;
import com.sdl.odata.api.edm.annotations.EdmParameter;
import com.sdl.odata.api.edm.annotations.EdmReturnType;
import com.sdl.odata.api.edm.model.Operation;
import com.sdl.odata.api.processor.datasource.factory.DataSourceFactory;
import com.sdl.odata.api.service.ODataRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
@EdmFunction(name = "Position", namespace = "Oberasoftware.Robot", isBound = true)
@EdmReturnType(type = "Edm.String")
public class PositionFunction implements Operation<String> {
    private static final Logger LOG = LoggerFactory.getLogger(PositionFunction.class);

    @EdmParameter(name = "servoId", nullable = false)
    private String servoId;

    @EdmParameter(name = "position", nullable = true)
    private Integer position;

    @EdmParameter(name = "speed", nullable = true)
    private Integer speed;

    @Override
    public String doOperation(ODataRequestContext oDataRequestContext, DataSourceFactory dataSourceFactory) throws ODataException {
        LOG.info("Setting servo: {} position: {} and speed: {}", servoId, position, speed);

        EventBus eventBus = ApplicationContextProvider.getContext().getBean(EventBus.class);
        if(position != null && speed != null) {
            eventBus.publish(new PositionAndSpeedCommand(servoId, position, speed));
        } else if(position != null) {
            eventBus.publish(new PositionCommand(servoId, position));
        } else if(speed != null) {
            eventBus.publish(new SpeedCommand(servoId, speed));
        } else {
            LOG.warn("Servo function called without setting speed or position parameters");
        }

        return "Setting servo position and speed for servo: " + servoId;
    }
}
