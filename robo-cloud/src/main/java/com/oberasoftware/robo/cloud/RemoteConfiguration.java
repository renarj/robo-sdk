package com.oberasoftware.robo.cloud;

import com.oberasoftware.home.core.mqtt.MQTTConfiguration;
import com.oberasoftware.home.util.UtilConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@ComponentScan
@Import({UtilConfiguration.class, MQTTConfiguration.class})
public class RemoteConfiguration {
}
