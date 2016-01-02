package com.oberasoftware.robo.pi4j;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class PI4JTest {
    private static final Logger LOG = LoggerFactory.getLogger(PI4JTest.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PI4JTest.class);

        try {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            I2CDevice device = bus.getDevice(0x68);
            byte[] buffer = new byte[4];
            while(true) {
                device.read(buffer, 0, 4);

                LOG.info("Returned bytes: {]", buffer);

                Thread.sleep(10000);
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }
    }
}
