package com.oberasoftware.robo.core;

import com.oberasoftware.base.BaseConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@Configuration
@ComponentScan
@Import(BaseConfiguration.class)
public class CoreConfiguration {
}
