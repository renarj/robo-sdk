package com.oberasoftware.robo.dynamixel.motion;

import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.MotionTask;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.motion.*;
import com.oberasoftware.robo.api.motion.controller.MotionController;
import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.core.motion.KeyFrameImpl;
import com.oberasoftware.robo.core.motion.ServoStepImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.empty;

/**
 * @author Renze de Vries
 */
@Component
public class RoboPlusMotionEngine implements MotionEngine {
    private static final Logger LOG = LoggerFactory.getLogger(RoboPlusMotionEngine.class);

    private static final String WALK_MOTION = "F_S_L";
    private static final String PREPARE_WALK = "Stand up";
    private static final String SIT_DOWN = "Sit down";

    @Autowired
    private RoboPlusMotionConverter roboPlusMotionConverter;

    @Autowired
    private JsonMotionLoader jsonMotionLoader;

    @Autowired
    private MotionManager motionManager;

    @Autowired
    private MotionExecutor motionExecutor;

    private ServoDriver servoDriver;

    private ConcurrentMap<String, MotionTask> runningTasks = new ConcurrentHashMap<>();

    private AtomicReference<MotionTask> walkingTask = new AtomicReference<>();

    private Lock lock = new ReentrantLock();

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        this.servoDriver = robot.getServoDriver();
    }

    @Override
    public void loadResource(MotionResource resource) {
        if(resource instanceof RoboPlusClassPathResource) {
            List<Motion> motions = roboPlusMotionConverter.loadMotions(((RoboPlusClassPathResource) resource).getPath());
            motions.forEach(motionManager::storeMotion);
        } else if(resource instanceof JsonMotionResource) {
            List<Motion> motions = jsonMotionLoader.loadMotions(((JsonMotionResource)resource).getPath());
            motions.forEach(motionManager::storeMotion);
        }
    }

    @Override
    public boolean prepareWalk() {
        return executeMotion(PREPARE_WALK, true) != null;
    }

    @Override
    public boolean rest() {
        return executeMotion(SIT_DOWN, true) != null;
    }

    @Override
    public MotionTask walkForward() {
        return walk(WalkDirection.FORWARD);
    }

    @Override
    public MotionTask walk(WalkDirection direction, float meters) {
        return null;
    }

    @Override
    public MotionTask goToPosture(String posture) {
        return null;
    }

    @Override
    public <T extends MotionController> Optional<T> getMotionController(String controllerName) {
        return empty();
    }

    @Override
    public MotionTask walk(WalkDirection direction) {
        MotionTask walkingTask = executeMotion(WALK_MOTION, false);
        this.walkingTask.set(walkingTask);

        return walkingTask;
    }

    @Override
    public KeyFrame getCurrentPositionAsKeyFrame() {
        KeyFrameImpl keyFrame = new KeyFrameImpl(Long.toString(System.currentTimeMillis()), 0);
        servoDriver.getServos().forEach(s -> {
            ServoData servoData = s.getData();
            keyFrame.addServoStep(new ServoStepImpl(s.getId(), servoData.getValue(ServoProperty.POSITION),
                    servoData.getValue(ServoProperty.SPEED)));
        });

        return keyFrame;
    }

    @Override
    public void shutdown() {
        stopAllTasks();
    }

    @Override
    public MotionTask runMotion(String motionName) {
        return executeMotion(motionName, false);
    }

    @Override
    public MotionTask runMotion(Motion motion) {
        return executeMotion(motion, false);
    }

    @Override
    public MotionTask runMotion(KeyFrame keyFrame) {
        return motionExecutor.execute(keyFrame);
    }

    private MotionTask executeMotion(String motionName, boolean await) {
        Optional<Motion> motion = motionManager.findMotionByName(motionName);
        motion.ifPresent(motion1 -> executeMotion(motion1, await));
        return null;
    }

    private MotionTask executeMotion(Motion motion, boolean await) {
        MotionTask task = motionExecutor.execute(motion);
        runningTasks.putIfAbsent(task.getTaskId(), task);

        if(await) {
            task.awaitCompletion();
        }
        return task;
    }

    @Override
    public List<MotionTask> getActiveTasks() {
        return newArrayList(runningTasks.values());
    }

    @Override
    public List<String> getMotions() {
        return motionManager.findMotions().stream()
                .map(Motion::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean stopWalking() {
        stopCurrentWalkingTask();
        return true;
    }

    @Override
    public boolean stopTask(MotionTask task) {
        LOG.info("Stopping motion task: {}", task);
        task.cancel();
        task.awaitCompletion();
        runningTasks.remove(task.getTaskId());

        return true;
    }

    @Override
    public boolean stopAllTasks() {
        getActiveTasks().forEach(this::stopTask);
        return true;
    }

    private void stopCurrentWalkingTask() {
        MotionTask task = walkingTask.get();
        if(task != null) {
            lock.lock();
            try {
                task.cancel();
                task.awaitCompletion();
                walkingTask.set(null);
            } finally {
                lock.unlock();
            }
        }
    }
}
