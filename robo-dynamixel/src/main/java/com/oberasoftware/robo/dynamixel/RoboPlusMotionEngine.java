package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.MotionResource;
import com.oberasoftware.robo.api.MotionTask;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionExecutor;
import com.oberasoftware.robo.core.robomotion.RoboPlusMotionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Renze de Vries
 */
@Component
public class RoboPlusMotionEngine implements MotionEngine {

    private static final String WALK_MOTION = "F_S_L";
    private static final String PREPARE_WALK = "Stand up";

    @Autowired
    private RoboPlusMotionConverter motionConverter;

    @Autowired
    private MotionManager motionManager;

    @Autowired
    private MotionExecutor motionExecutor;

    private ConcurrentMap<String, MotionTask> runningTasks = new ConcurrentHashMap<>();

    private AtomicReference<MotionTask> walkingTask = new AtomicReference<>();

    private Lock lock = new ReentrantLock();

    @Override
    public void loadResource(MotionResource resource) {
        if(resource instanceof RoboPlusClassPathResource) {
            List<Motion> motions = motionConverter.loadMotions(((RoboPlusClassPathResource) resource).getPath());
            motions.stream().forEach(motionManager::storeMotion);
        }
    }

    @Override
    public boolean prepareWalk() {
        return executeMotion(PREPARE_WALK, true) != null;
    }

    @Override
    public MotionTask walk() {
        MotionTask walkingTask = executeMotion(WALK_MOTION, false);
        this.walkingTask.set(walkingTask);

        return walkingTask;
    }

    private MotionTask executeMotion(String motionName, boolean await) {
        Optional<Motion> motion = motionManager.findMotionByName(motionName);
        if(motion.isPresent()) {
            MotionTask task = motionExecutor.execute(motion.get());
            runningTasks.putIfAbsent(task.getTaskId(), task);

            if(await) {
                task.awaitCompletion();
            }
            return task;
        }
        return null;
    }

    @Override
    public List<MotionTask> getActiveTasks() {
        return newArrayList(runningTasks.values());
    }

    @Override
    public boolean stopWalking() {
        stopCurrentWalkingTask();
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
