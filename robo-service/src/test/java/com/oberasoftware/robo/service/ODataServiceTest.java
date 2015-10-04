package com.oberasoftware.robo.service;

import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.RobotController;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.core.CoreConfiguration;
import com.oberasoftware.robo.api.MotionConverter;
import com.oberasoftware.robo.service.model.MotionModel;
import com.oberasoftware.robo.service.model.ServoModel;
import com.sdl.odata.api.edm.registry.ODataEdmRegistry;
import com.sdl.odata.service.ODataServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Renze de Vries
 */
@EnableAutoConfiguration
@Configuration
@Import({
        ODataServiceConfiguration.class,
        CoreConfiguration.class,
        ServiceConfiguration.class
})
@ComponentScan
public class ODataServiceTest {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ODataServiceTest.class);
        springApplication.setShowBanner(false);
        ConfigurableApplicationContext context = springApplication.run(args);

        ODataEdmRegistry registry = context.getBean(ODataEdmRegistry.class);
        registry.registerClasses(newArrayList(MotionModel.class, ServoModel.class, MotionFunction.class));

        RobotController controller = context.getBean(RobotController.class);
        MotionManager motionManager = context.getBean(MotionManager.class);
        MotionConverter motionConverter = context.getBean(MotionConverter.class);

        List<Motion> motions = motionConverter.loadMotions("/bio_prm_kingspider_en.mtn");
        motions.stream().forEach(motionManager::storeMotion);
    }
}
