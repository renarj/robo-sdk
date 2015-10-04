package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.motion.Motion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Renze de Vries
 */
@Component
public class MotionManagerImpl implements MotionManager {

    private ConcurrentMap<String, Motion> motions = new ConcurrentHashMap<>();

    @Override
    public void storeMotion(Motion motion) {
        this.motions.putIfAbsent(motion.getName(), motion);
    }

    @Override
    public Optional<Motion> findMotion(String motionName) {
        return Optional.ofNullable(motions.get(motionName));
    }

    @Override
    public List<Motion> findMotions() {
        return new ArrayList<>(motions.values());
    }
}
