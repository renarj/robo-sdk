package com.oberasoftware.robo.service.mock;

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
    public void execute(Motion motion) {
        execute(motion, 0);
    }

    @Override
    public void execute(Motion motion, int repeats) {
        LOG.debug("Received a motion request: {} repeats: {}", motion, repeats);
    }
}
