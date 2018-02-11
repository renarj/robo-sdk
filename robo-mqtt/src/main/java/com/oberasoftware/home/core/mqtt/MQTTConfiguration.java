package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.BaseConfiguration;
import com.oberasoftware.home.util.UtilConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@Import({UtilConfiguration.class, BaseConfiguration.class})
@ComponentScan
public class MQTTConfiguration {
}
