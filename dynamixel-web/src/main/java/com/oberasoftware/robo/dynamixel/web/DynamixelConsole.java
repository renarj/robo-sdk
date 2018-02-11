package com.oberasoftware.robo.dynamixel.web;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.robo.dynamixel.DynamixelConfiguration;
import com.oberasoftware.robo.dynamixel.web.test.PosData;
import com.oberasoftware.robo.dynamixel.web.test.WalkerTest;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@SpringBootApplication
@Import(DynamixelConfiguration.class)
public class DynamixelConsole {
    private static final Logger LOG = getLogger(DynamixelConsole.class);

    public static void main(String[] args) {
        LOG.info("Starting Dynamixel Web Console");

        WalkerTest w = SpringApplication.run(DynamixelConsole.class, args).getBean(WalkerTest.class);
        LOG.info("Constructed");

        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        LOG.info("Doing test");


        for(int i=0; i<10; i++) {
            w.test(new PosData(0, 0, 0), new PosData(0,0, 0));
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            w.test(new PosData(0, 0, 4), new PosData(0,0, 0));
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            w.test(new PosData(0, 0, 0), new PosData(0,0, 0));
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);


            w.test(new PosData(0, 0, -4), new PosData(0,0, 0));
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            LOG.info("Iteration: {} complete", i);
        }
        LOG.info("Iterations complete");

//        w.test(new PosData(0, 0, 5), new PosData(0,0, 0));
//        Uninterruptibles.sleepUninterruptibly(15, TimeUnit.SECONDS);
////
//        w.test(new PosData(0, 0, 0), new PosData(0,0, 0));
//        Uninterruptibles.sleepUninterruptibly(15, TimeUnit.SECONDS);
//
//        w.test(new PosData(0, 0, -5), new PosData(0,0, 0));
//        Uninterruptibles.sleepUninterruptibly(15, TimeUnit.SECONDS);
//
//
        w.stop();
//
//        System.exit(-1);
    }
}
