package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.core.CoreConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@ComponentScan
@Import(CoreConfiguration.class)
public class DynamixelConfiguration {
}
