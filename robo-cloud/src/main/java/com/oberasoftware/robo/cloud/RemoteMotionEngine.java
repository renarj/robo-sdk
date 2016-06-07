package com.oberasoftware.robo.cloud;

import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.MotionTask;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.motion.MotionResource;
import com.oberasoftware.robo.api.motion.WalkDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.oberasoftware.home.api.model.BasicCommandBuilder.create;

/**
 * @author Renze de Vries
 */
@Component
@Scope("prototype")
public class RemoteMotionEngine implements MotionEngine {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteMotionEngine.class);

    private Robot robot;

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
    public List<String> getMotions() {
        return null;
    }

    @Override
    public void loadResource(MotionResource resource) {

    }

    @Override
    public MotionTask walkForward() {
        return null;
    }

    @Override
    public MotionTask walk(WalkDirection direction) {
        return null;
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
    public List<MotionTask> getActiveTasks() {
        return null;
    }

    @Override
    public boolean stopTask(MotionTask task) {
        return false;
    }

    @Override
    public boolean stopAllTasks() {
        return false;
    }

    @Override
    public boolean stopWalking() {
        return false;
    }

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        LOG.info("Activating remote motion engine for robot: {}", robot.getName());
        this.robot = robot;
    }

    @Override
    public void shutdown() {
        LOG.info("Doing shutdown of robot");
        rest();
    }
}
