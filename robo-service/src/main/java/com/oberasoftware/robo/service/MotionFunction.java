package com.oberasoftware.robo.service;

import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionExecutor;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.annotations.EdmFunction;
import com.sdl.odata.api.edm.annotations.EdmParameter;
import com.sdl.odata.api.edm.annotations.EdmReturnType;
import com.sdl.odata.api.edm.model.Operation;
import com.sdl.odata.api.processor.datasource.factory.DataSourceFactory;
import com.sdl.odata.api.service.ODataRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
@EdmFunction(name = "Execute", namespace = "Oberasoftware.Robot", isBound = true)
@EdmReturnType(type = "Edm.String")
public class MotionFunction implements Operation<String> {
    private static final Logger LOG = LoggerFactory.getLogger(MotionFunction.class);

    @EdmParameter(name = "repeats", nullable = false)
    private int repeats;

    @EdmParameter(name = "motion", nullable = false)
    private String motionName;

    @Override
    public String doOperation(ODataRequestContext requestContext, DataSourceFactory dataSourceFactory) throws ODataException {
        LOG.info("Executing motion: {}, repeats: {}", motionName, repeats);

        MotionManager motionManager = ApplicationContextProvider.getContext().getBean(MotionManager.class);
        MotionExecutor motionExecutor = ApplicationContextProvider.getContext().getBean(MotionExecutor.class);

        Optional<Motion> motion = motionManager.findMotionByName(motionName);
        if(motion.isPresent()) {
            LOG.info("Motion was found, triggering movement");
            motionExecutor.execute(motion.get(), repeats);

            return "Executing Motion: " + motionName;
        }

        return "Could not find motion: " + motionName;
    }
}
