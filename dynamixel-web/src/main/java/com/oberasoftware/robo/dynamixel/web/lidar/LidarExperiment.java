package com.oberasoftware.robo.dynamixel.web.lidar;

import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.core.commands.OperationModeCommand;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.DynamixelServoDriver;
import com.oberasoftware.robo.dynamixel.SerialDynamixelConnector;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import com.oberasoftware.robo.dynamixel.web.DynamixelConsole;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static com.oberasoftware.robo.core.ConverterUtil.byteToInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address.RETURN_DELAY_TIME;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

public class LidarExperiment {
    private static final Logger LOG = getLogger(LidarExperiment.class);

    public static void main(String[] args) {
        LOG.info("Starting Dynamixel Web Console");
        ConfigurableApplicationContext c = SpringApplication.run(DynamixelConsole.class, args);

        DynamixelServoDriver servoDriver = c.getBean(DynamixelServoDriver.class);
        SerialDynamixelConnector connector = c.getBean(SerialDynamixelConnector.class);

        byte[] powerPackage = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, 200)
                .add8BitParam(DynamixelV2Address.POWER, 1).build();
        byte[] packet = connector.sendAndReceive(powerPackage);
        LOG.info("Return power packet: {} / {}", bb2hex(packet), new DynamixelV2ReturnPacket(packet));

        sleepUninterruptibly(1, TimeUnit.SECONDS);

        byte[] readSensor = new DynamixelV2CommandPacket(DynamixelInstruction.READ_DATA, 200)
                .addInt16Bit(DynamixelV2Address.LIDAR, 0x04).build();
        LOG.info("Sending package: {}", bb2hex(readSensor));

        servoDriver.setTorgue("50", false);

        connector.sendAndReceive(new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, 200).add8BitParam(RETURN_DELAY_TIME, 0x0).build());
        connector.sendAndReceive(new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, 50).add8BitParam(RETURN_DELAY_TIME, 0x0).build());
        servoDriver.sendCommand(new OperationModeCommand("50", OperationModeCommand.MODE.EXTENDED_POSITION_CONTROL));


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            servoDriver.setServoSpeed("50", 0, new Scale(0, 0));
        }));

        RosClient ros = new RosClient();
        ros.connect();

        servoDriver.setTorgue("50", false);
        servoDriver.sendCommand(new OperationModeCommand("50", OperationModeCommand.MODE.VELOCITY_MODE));


        servoDriver.setTorgue("50", true);
        servoDriver.setServoSpeed("50", 25, new Scale(0, 0));
        LOG.info("Ready for scan");

        Servo servo = servoDriver.getServo("50");

        boolean keepScanning = true;
        int seq = 0;
        while(keepScanning) {
            seq++;

            List<Double> scanValues = new ArrayList<>();
            long start = System.currentTimeMillis();

            int initialPosition = servo.getData().getValue(ServoProperty.POSITION);

            boolean scanActive = true;
            while (scanActive) {
                byte[] received = connector.sendAndReceive(readSensor);

                int position = servo.getData().getValue(ServoProperty.POSITION);

                DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
                if (!returnPacket.hasErrors()) {
                    byte[] params = returnPacket.getParameters();
                    int value = byteToInt(params[0], params[1]);
                    LOG.info("Distance: {} at position: {}", value, position);

                    scanValues.add(value / 100.0);
                }

                if(position - initialPosition > 8190) {
                    LOG.info("Reached a full cycle, killing");
                    scanActive = false;
                }

                sleepUninterruptibly(50, TimeUnit.MILLISECONDS);
            }

            int endPosition = servo.getData().getValue(ServoProperty.POSITION);
            long end = System.currentTimeMillis();
            LOG.info("Scan completed in: {} ms. with values: {}", end - start, scanValues);
            LOG.info("Start position: {} end position: {} delta: {}", initialPosition, endPosition, (endPosition - initialPosition));

            LaserScan.Header header = new LaserScan.Header(seq, new LaserScan.RosTime(System.currentTimeMillis(), 0), "myFrame");

            double degreePerUnit = 0.088;

            int delta = endPosition - initialPosition;
            double degrees = 360 + ((delta % 8190) * degreePerUnit);
            double rads = Math.toRadians(degrees);
            double increment = rads / scanValues.size();

            double timeDelta = ((end - start) / (double)scanValues.size()) / 1000.0;

            LOG.info("Degrees: {} Rads: {} increment: {} time delta: {}", degrees, rads, increment, timeDelta);

            double[] ranges = scanValues.stream().mapToDouble(Double::doubleValue).toArray();

            LaserScan scan = new LaserScan(0.0, rads, increment, timeDelta, (double)delta/1000.0, 0.0, 10.0, ranges, new double[]{}, header);
            ros.sendLaserScan(scan);
        }

    }
}
