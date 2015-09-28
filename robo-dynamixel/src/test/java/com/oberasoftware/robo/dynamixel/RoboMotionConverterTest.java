package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.dynamixel.robomotion.RoboPlusMotionConverter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
public class RoboMotionConverterTest {
    @Test
    public void testConvert() {
        List<Motion> motions = new RoboPlusMotionConverter().loadMotions("/bio_prm_kingspider_en.mtn");
        assertThat(motions.size(), is(17));


        Map<String, Motion> motionMap = motions.stream().collect(Collectors.toMap(Motion::getName, m -> m));
        Set<String> motionNames = motionMap.keySet();
        assertThat(motionNames, hasItems("Ready", "Forward walk", "Fast F walk", "Backward walk",
                "Fast B walk", "Turn right", "Fast R turn",
                "Turn left", "Fast L turn", "Sit down", "Fear", "Attack ready",
                "Attack front", "Behavior1", "Behavior2", "Behavior3", "Behavior4"));

        Motion readyMotion = motionMap.get("Ready");
        assertThat(readyMotion.getSteps().size(), is(1));
        assertThat(readyMotion.getSteps().get(0).getServoSteps().size(), is(18));
        assertThat(readyMotion.getSteps().get(0).getTimeInMs(), is(504l));
    }
}
