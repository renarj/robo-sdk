package com.oberasoftware.robo.dynamixel.web;

import com.oberasoftware.robo.dynamixel.DynamixelConfiguration;
import com.oberasoftware.robo.dynamixel.DynamixelServoDriver;
import com.oberasoftware.robo.dynamixel.SerialDynamixelConnector;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

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
        ConfigurableApplicationContext c = SpringApplication.run(DynamixelConsole.class, args);

        DynamixelServoDriver servoDriver = c.getBean(DynamixelServoDriver.class);
        SerialDynamixelConnector connector = c.getBean(SerialDynamixelConnector.class);


        for(int i=0; i<10; i++) {
            LOG.info("Scanning: {}", i);
            servoDriver.scan();
        }

        LOG.info("All scans complete");
//        servoDriver.sendCommand(new WriteIDCommand("1", "141"));


//        connector.sendAndReceive(new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA,  0x01).add8BitParam(DynamixelV2Address.ID))


//        connector.sendAndReceive(new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, 200)
//                .add8BitParam(DynamixelV2Address.POWER, 0x01)
//                .build());
//
//
//        servoDriver.getServos().forEach(s -> servoDriver.sendCommand(new OperationModeCommand(s.getId(), OperationModeCommand.MODE.POSITION_CONTROL)));
//        servoDriver.getServos().forEach(Servo::enableTorgue);
//        servoDriver.getServos().forEach(s -> s.moveTo(50, new Scale(-100, 100)));
//
//
//        servoDriver.sendCommand(new VelocityModeCommand("24", 200, 50));
//        servoDriver.sendCommand(new VelocityModeCommand("50", 0, 0));
//
//        servoDriver.getServos().forEach(s -> s.moveTo(0, new Scale(-100, 100)));
//
//        servoDriver.getServos().forEach(Servo::disableTorgue);
    }
}
