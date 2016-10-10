package com.oberasoftware.robo.service.mock;

import com.oberasoftware.robo.api.MotionTask;
import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class MockMotionExecutor implements MotionExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(MockMotionExecutor.class);

    @Override
    public MotionTask execute(Motion motion) {
        LOG.debug("Received a motion request: {}", motion);
        return null;
    }

    @Override
    public MotionTask execute(KeyFrame keyFrame) {
        LOG.debug("Received a keyframe request: {}", keyFrame);
        return null;
    }
}
