package com.oberasoftware.robo.cloud.motion;

import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.MotionTask;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionResource;
import com.oberasoftware.robo.api.motion.WalkDirection;
import com.oberasoftware.robo.api.motion.controller.MotionController;
import com.oberasoftware.robo.cloud.motion.controllers.RemoteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.oberasoftware.robo.core.model.BasicCommandBuilder.create;
import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * @author Renze de Vries
 */
@Component
@Scope("prototype")
public class RemoteMotionEngine implements MotionEngine {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteMotionEngine.class);

    private Robot robot;

    @Autowired
    private List<RemoteController> remoteControllers;

    @Override
    public boolean prepareWalk() {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("prepare")
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public boolean rest() {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("rest")
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public <T extends MotionController> Optional<T> getMotionController(String controllerName) {
        Optional<RemoteController> remoteController = remoteControllers.stream()
                .filter(r -> r.getName().equalsIgnoreCase(controllerName)).findFirst();
        if(remoteController.isPresent()) {
            return of((T)remoteController.get());
        } else {
            return empty();
        }
    }

    @Override
    public List<String> getMotions() {
        return null;
    }

    @Override
    public void loadResource(MotionResource resource) {

    }

    @Override
    public KeyFrame getCurrentPositionAsKeyFrame() {
        return null;
    }

    @Override
    public MotionTask walkForward() {
        return walk(WalkDirection.FORWARD);
    }

    @Override
    public MotionTask walk(WalkDirection direction) {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("walk")
                .property("direction", direction.name())
                .build();

        robot.getRemoteDriver().publish(command);

        return null;
    }

    @Override
    public MotionTask walk(WalkDirection direction, float meters) {
        return walk(WalkDirection.FORWARD);
    }

    @Override
    public MotionTask runMotion(String motionName) {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("run")
                .property("motion", motionName)
                .build();

        robot.getRemoteDriver().publish(command);
        return null;
    }

    @Override
    public MotionTask runMotion(Motion motion) {
        return null;
    }

    @Override
    public MotionTask runMotion(KeyFrame keyFrame) {
        return null;
    }

    @Override
    public MotionTask goToPosture(String posture) {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("set")
                .property("posture", posture)
                .build();

        robot.getRemoteDriver().publish(command);

        return null;
    }

    @Override
    public List<MotionTask> getActiveTasks() {
        return null;
    }

    @Override
    public boolean stopTask(MotionTask task) {
        return false;
    }

    @Override
    public boolean stopAllTasks() {
        BasicCommand command = create(robot.getName())
                .item("motion")
                .label("stop")
                .build();
        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public boolean stopWalking() {
        walk(WalkDirection.STOP);

        return false;
    }

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        LOG.info("Activating remote motion engine for robot: {}", robot.getName());
        this.robot = robot;

        remoteControllers.forEach(rc -> rc.activate(robot));
    }

    @Override
    public void shutdown() {
        LOG.info("Doing shutdown of robot");
        rest();
    }
}
