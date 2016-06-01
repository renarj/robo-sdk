package com.oberasoftware.robo.core;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Renze de Vries
 */
@Component
public class RobotRegistryImpl implements RobotRegistry {

    private ConcurrentMap<String, Robot> robotConcurrentMap = new ConcurrentHashMap<>();

    @Override
    public Robot getRobot(String name) {
        return robotConcurrentMap.get(name);
    }

    @Override
    public List<Robot> getRobots() {
        return new ArrayList<>(robotConcurrentMap.values());
    }

    @Override
    public void register(Robot robot) {
        robotConcurrentMap.putIfAbsent(robot.getName(), robot);
    }
}
