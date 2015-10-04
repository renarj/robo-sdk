/**
 * Copyright (c) 2015 SDL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oberasoftware.robo.service;

import com.oberasoftware.base.BaseConfiguration;
import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.RobotController;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.dynamixel.DynamixelConfiguration;
import com.oberasoftware.robo.dynamixel.robomotion.MotionConverter;
import com.oberasoftware.robo.service.model.MotionModel;
import com.oberasoftware.robo.service.model.ServoModel;
import com.sdl.odata.api.edm.registry.ODataEdmRegistry;
import com.sdl.odata.service.ODataServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rdevries
 */
@Configuration
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class })
@Import({
        ODataServiceConfiguration.class,
        DynamixelConfiguration.class,
        BaseConfiguration.class

})
@ComponentScan
public class ServiceContainer {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceContainer.class);

    public static void main(String[] args) {
        LOG.info("Starting Robot Service Application container");

        SpringApplication springApplication = new SpringApplication(ServiceContainer.class);
        springApplication.setShowBanner(false);
        ConfigurableApplicationContext context = springApplication.run(args);

        ODataEdmRegistry registry = context.getBean(ODataEdmRegistry.class);
        registry.registerClasses(newArrayList(MotionModel.class, ServoModel.class));

        RobotController controller = context.getBean(RobotController.class);
        MotionManager motionManager = context.getBean(MotionManager.class);
        MotionConverter motionConverter = context.getBean(MotionConverter.class);

        List<Motion> motions = motionConverter.loadMotions("/bio_prm_kingspider_en.mtn");
        motions.stream().forEach(motionManager::storeMotion);

    }

}
