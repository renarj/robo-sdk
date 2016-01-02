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

//    public static void main(String[] args) {
//        double i = 10.1;
//
//        LOG.info("I: {}", i);
//
//        int h = 0 & 0xf;
//        int m = -78 & 0xf;
//        int l = 108 & 0xf;
//        int s = 28 & 0xf;
//
//        LOG.info("M: {}", m);
//
//        long upper = (h << 16) + (m << 8);
//        long lower = (l << 16) + (s << 8);
//
//        LOG.info("Upper: {}", upper);
//        LOG.info("Lower: {}", lower);
//
//        LOG.info("Result: {}", Double.longBitsToDouble((upper << 32) + lower));
//    }
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PI4JTest.class);

        try {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            I2CDevice device = bus.getDevice(0x68);

            device.write((byte)0x9C);

            byte[] buffer = new byte[4];
            while(true) {
                device.read(buffer, 0, 4);

//                LOG.info("Returned bytes: {]", buffer);

                int h = buffer[0];
                int m = buffer[1];
                int l = buffer[2];
                int s = buffer[3];

//                int t = ((h & 0x00000011) << 16) | (m << 8) | l;
                LOG.info("Val h: {} m: {} l: {} s: {}", h, m, l, s);

                int maskedH = h & 3;
                int shiftH = maskedH << 16;
                int shiftM = m << 8;

                LOG.info("Masked H: {} shifted H: {} shifted M: {}", maskedH, shiftH, shiftM);
                int t = shiftH | shiftM | l;
                LOG.info("Result bitwise OR: {}", t);

                //sign bit = 17
                int sb = 17;
                boolean signbit = ((t & (1 << sb)) != 0);
                if(signbit) {
                    t = t & ~(1 << sb);
                    float pga = 0.5f;
                    float lsb = 0.0000078125f;

                    double voltage = (t * (lsb / pga)) * 2.471;
                    LOG.info("Voltage: {}", voltage);
                } else {
                    LOG.info("Incorrect value?");
                }

                Thread.sleep(10000);
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }
    }
}
