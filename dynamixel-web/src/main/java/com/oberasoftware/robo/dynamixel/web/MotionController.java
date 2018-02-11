package com.oberasoftware.robo.dynamixel.web;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionManager;
import com.oberasoftware.robo.core.motion.KeyFrameImpl;
import com.oberasoftware.robo.core.motion.MotionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
@RestController
@RequestMapping("/motions")
public class MotionController {
    private static final Logger LOG = LoggerFactory.getLogger(MotionController.class);

    @Autowired
    private RobotRegistry robotRegistry;

    @Autowired
    private MotionManager motionManager;

    @RequestMapping(value = "/keyframe", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public KeyFrame getKeyFrame() {
        LOG.info("Retrieving current servo positions as keyframe");
        return getDefaultRobot().getMotionEngine().getCurrentPositionAsKeyFrame();
    }

    @RequestMapping(value = "/load/{motionId}", method = RequestMethod.GET,
            consumes = "application/json", produces = "application/json")
    public Motion getMotion(@PathVariable  String motionId) {
        Optional<Motion> motion = motionManager.findMotionById(motionId);
        return motion.isPresent() ? motion.get() : null;
    }

    @RequestMapping(value = "/run/{motionName}", method = RequestMethod.POST)
    public void runMotion(@PathVariable String motionName) {
        LOG.info("Triggering motion: {} execution", motionName);
        getDefaultRobot().getMotionEngine().runMotion(motionName);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public void runMotion() {
        LOG.info("Triggering stop motions");
        getDefaultRobot().getMotionEngine().stopAllTasks();
    }

    @RequestMapping(value = "/store/motion", method = RequestMethod.POST)
    public Motion storeMotion(@RequestBody MotionImpl motion) {
        motionManager.storeMotion(motion);

        return motion;
    }

    @RequestMapping(value = "/run/keyframes", method = RequestMethod.POST)
    public Motion runKeyFrames(@RequestBody MotionImpl motion) {
        getDefaultRobot().getMotionEngine().runMotion(motion);

        return motion;
    }

    @RequestMapping(value = "/run/keyframe", method = RequestMethod.POST)
    public KeyFrame setKeyFrame(@RequestBody KeyFrameImpl keyFrame) {
        getDefaultRobot().getMotionEngine().runMotion(keyFrame);

        return keyFrame;
    }

    private Robot getDefaultRobot() {
        return robotRegistry.getRobots().get(0);
    }
}
