package com.oberasoftware.robo.dynamixel.web;

import com.google.common.base.Stopwatch;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.servo.Servo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
@Controller
public class DashboardController {
    private static final Logger LOG = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private RobotRegistry robotRegistry;

    @RequestMapping("/")
    public String getIndex(Model model) {
        LOG.info("Index was requested");
        Stopwatch w = Stopwatch.createStarted();
        List<Robot> robots = robotRegistry.getRobots();


        Stopwatch s = Stopwatch.createStarted();
        List<Servo> servos = robots.stream().map(r -> r.getServoDriver().getServos())
                .flatMap(Collection::stream).collect(Collectors.toList());
        LOG.info("Servo request took: {}", s.elapsed(TimeUnit.MILLISECONDS));

        model.addAttribute("servos", servos.stream()
                .map(SimpleServo::new)
                .collect(Collectors.toList()));

        LOG.info("Index request done in: {} ms.", w.elapsed(TimeUnit.MILLISECONDS));
        return "index";
    }
}
