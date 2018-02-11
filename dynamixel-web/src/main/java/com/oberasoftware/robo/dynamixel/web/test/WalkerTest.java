package com.oberasoftware.robo.dynamixel.web.test;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.dynamixel.commands.DynamixelAngleLimitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

@Component
public class WalkerTest {
    private static final Logger LOG = LoggerFactory.getLogger(WalkerTest.class);

    private static final BiFunction<Double, Double, Double> xFunction = (coxaLength, femurLength) -> Math.cos(60.0/180.0 * Math.PI) *  (coxaLength + femurLength);
    private static final BiFunction<Double, Double, Double> xNegativeFunction = (coxaLength, femurLength) -> -(Math.cos(60.0/180.0 * Math.PI) * (coxaLength + femurLength));
    private static final BiFunction<Double, Double, Double> yFunction = (coxaLength, femurLength) -> Math.sin(60.0/180.0 * Math.PI) * (coxaLength + femurLength);
    private static final BiFunction<Double, Double, Double> yNegativeFunction = (coxaLength, femurLength) -> Math.sin(-60.0/180.0 * Math.PI) * (coxaLength + femurLength);


    private static final double DYNA_DEGREE_CONVERSION = 512.0/180.0;

    @Autowired
    private RobotRegistry robotRegistry;

//    public static void main(String[] args) {
//        WalkerTest w = new WalkerTest();
//
//        LOG.info("Iteration 1");
//
//        w.test(new PosData(0, 0, -5), new PosData(0,0, 0));
////        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
//
////        LOG.info("Iteration 2");
////        w.test(new PosData(0, 0, 0), new PosData(0,0, 0));
//////        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
////
////        LOG.info("Iteration 3");
////        w.test(new PosData(0, 0, 5), new PosData(0,0, 0));
//////        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
//
//
//
//    }

    public void stop() {
        robotRegistry.getRobots().get(0).getServoDriver().getServos().forEach(Servo::disableTorgue);
    }

    public void test(PosData dirInput, PosData rotInput) {

        Robot robot = robotRegistry.getRobots().get(0);
//        ServoDriver servoDriver = new ServoDriver() {
//            @Override
//            public boolean setServoSpeed(String servoId, int speed) {
//                return false;
//            }
//
//            @Override
//            public boolean setTargetPosition(String servoId, int targetPosition) {
//                return false;
//            }
//
//            @Override
//            public boolean setPositionAndSpeed(String servoId, int speed, int targetPosition) {
//                return false;
//            }
//
//            @Override
//            public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands) {
//                return false;
//            }
//
//            @Override
//            public boolean supportsCommand(ServoCommand servoCommand) {
//                return false;
//            }
//
//            @Override
//            public boolean sendCommand(ServoCommand servoCommand) {
//                return false;
//            }
//
//            @Override
//            public boolean setTorgue(String servoId, int limit) {
//                return false;
//            }
//
//            @Override
//            public boolean setTorgue(String servoId, boolean state) {
//                return false;
//            }
//
//            @Override
//            public List<Servo> getServos() {
//                return null;
//            }
//
//            @Override
//            public Servo getServo(String servoId) {
//                return null;
//            }
//
//            @Override
//            public void activate(Robot robot, Map<String, String> properties) {
//
//            }
//
//            @Override
//            public void shutdown() {
//
//            }
//        };
        ServoDriver servoDriver = robot.getServoDriver();
        servoDriver.getServos().forEach(s -> {
            servoDriver.sendCommand(new DynamixelAngleLimitCommand(s.getId(), DynamixelAngleLimitCommand.MODE.JOINT_MODE));
        });
        servoDriver.getServos().forEach(s -> s.setSpeed(100));
        servoDriver.getServos().forEach(Servo::enableTorgue);



//        PosData dirInput = new PosData(0, 0, 5);
//        PosData rotInput = new PosData(0,0,0);

        HexRobotBaseData baseData = new HexRobotBaseData(4.8, 6.71, 13.5,
                31, 15, 20);

        PosData leg1PosData = createInitialFeetPosition(baseData, xFunction, yFunction);
        PosData leg2PosData = createInitialFeetPosition(baseData,
                (coxaLength, femurLength) -> coxaLength + femurLength,
                (coxaLength, femurLength) -> 0.0);
        PosData leg3PosData = createInitialFeetPosition(baseData,
                xFunction, yNegativeFunction);
        PosData leg4PosData = createInitialFeetPosition(baseData,
                xNegativeFunction, yNegativeFunction);
        PosData leg5PosData = createInitialFeetPosition(baseData,
                (coxaLength, femurLength) -> -(coxaLength + femurLength),
                (coxaLength, femurLength) -> 0.0);
        PosData leg6PosData = createInitialFeetPosition(baseData,
                xNegativeFunction, yFunction);



        Leg leg1 = new Leg("TopRight", 7.5, 15.5, leg1PosData, servoDriver.getServo("8"), servoDriver.getServo("10"), servoDriver.getServo("3"), -60.0, 1); //is ok
        Leg leg2 = new Leg("MiddleRight", 10, 0, leg2PosData, servoDriver.getServo("15"), servoDriver.getServo("5"), servoDriver.getServo("16"), 0.0, -1);
        Leg leg3 = new Leg("BottomRight", 7.5, -15.5, leg3PosData, servoDriver.getServo("18"), servoDriver.getServo("14"), servoDriver.getServo("1"), 60.0, -1);
        Leg leg4 = new Leg("BottomLeft", -7.5,-15.5, leg4PosData, servoDriver.getServo("7"), servoDriver.getServo("2"), servoDriver.getServo("4"), -240.0, 1); // is ok
        Leg leg5 = new Leg("MiddleLeft", -10, 0, leg5PosData, servoDriver.getServo("12"), servoDriver.getServo("6"), servoDriver.getServo("9"), -180.0, -1);
        Leg leg6 = new Leg("TopLeft", -7.5, 15.5, leg6PosData, servoDriver.getServo("11"), servoDriver.getServo("13"), servoDriver.getServo("17"), -120.0, -1);

        List<Leg> legs = Lists.newArrayList(leg1, leg2, leg3, leg4, leg5, leg6);

        LOG.info("Base data for Leg 1: {}", leg1);
        LOG.info("Base data for Leg 2: {}", leg2);
        LOG.info("Base data for Leg 3: {}", leg3);
        LOG.info("Base data for Leg 4: {}", leg4);
        LOG.info("Base data for Leg 5: {}", leg5);
        LOG.info("Base data for Leg 6: {}", leg6);

        Map<String, PositionAndSpeedCommand> m = new HashMap<>();
        legs.forEach(l -> {
            PosData bodyIKLeg = createBodyIKLeg(l, dirInput, rotInput);
            AngleData angles = createIKLeg(l, baseData, bodyIKLeg, dirInput);
            LOG.info("Leg: {} angles: {}", l.getLegId(), angles);

//            l.getCoxia().moveTo((int)angles.getCoxaAngle());
//            l.getFemur().moveTo((int)angles.getFemurAngle());
//            l.getTibia().moveTo((int)angles.getTibiaAngle());

            m.put(l.getCoxia().getId(), new PositionAndSpeedCommand(l.getCoxia().getId(), (int)angles.getCoxaAngle(), 200));
            m.put(l.getFemur().getId(), new PositionAndSpeedCommand(l.getFemur().getId(), (int)angles.getFemurAngle(), 200));
            m.put(l.getTibia().getId(), new PositionAndSpeedCommand(l.getTibia().getId(), (int)angles.getTibiaAngle(), 200));
        });

        servoDriver.bulkSetPositionAndSpeed(m);


//        PosData bodyIKLeg1 = createBodyIKLeg(leg1, dirInput, rotInput);
//        PosData bodyIKLeg2 = createBodyIKLeg(leg2, dirInput, rotInput);
//        PosData bodyIKLeg3 = createBodyIKLeg(leg3, dirInput, rotInput);
//        PosData bodyIKLeg4 = createBodyIKLeg(leg4, dirInput, rotInput);
//        PosData bodyIKLeg5 = createBodyIKLeg(leg5, dirInput, rotInput);
//        PosData bodyIKLeg6 = createBodyIKLeg(leg6, dirInput, rotInput);
//
//        AngleData leg1Angle = createIKLeg(leg1, baseData, bodyIKLeg1, dirInput);
//        AngleData leg2Angle = createIKLeg(leg2, baseData, bodyIKLeg2, dirInput);
//        AngleData leg3Angle = createIKLeg(leg3, baseData, bodyIKLeg3, dirInput);
//        AngleData leg4Angle = createIKLeg(leg4, baseData, bodyIKLeg4, dirInput);
//        AngleData leg5Angle = createIKLeg(leg5, baseData, bodyIKLeg5, dirInput);
//        AngleData leg6Angle = createIKLeg(leg6, baseData, bodyIKLeg6, dirInput);
//
//        LOG.info("Leg 1 angles: {}", leg1Angle);
//        LOG.info("Leg 2 angles: {}", leg2Angle);
//        LOG.info("Leg 3 angles: {}", leg3Angle);
//        LOG.info("Leg 4 angles: {}", leg4Angle);
//        LOG.info("Leg 5 angles: {}", leg5Angle);
//        LOG.info("Leg 6 angles: {}", leg6Angle);
//
//
//
//        leg6.getCoxia().moveTo((int)leg6Angle.getCoxaAngle());
//        leg6.getFemur().moveTo((int)leg6Angle.getFemurAngle());
//        leg6.getTibia().moveTo((int)leg6Angle.getTibiaAngle());
    }



    private static PosData createInitialFeetPosition(HexRobotBaseData baseData, BiFunction<Double, Double, Double> xFunction, BiFunction<Double, Double, Double> yFunction) {
        double x = xFunction.apply(baseData.getCoxaLength(), baseData.getFemurLength());
        double y = yFunction.apply(baseData.getCoxaLength(), baseData.getFemurLength());

        return new PosData(x, y, baseData.getTibiaLength());
    }

    private static PosData createBodyIKLeg(Leg leg, PosData xyzInput, PosData rotInput) {
        double totalY = leg.getInitialPosition().getY() + leg.getOffsetY() + xyzInput.getY();
        double totalX = leg.getInitialPosition().getX() + leg.getOffsetX() + xyzInput.getX();
        double distBodyCenterFeet = Math.sqrt(Math.pow(totalY, 2) + Math.pow(totalX, 2));
        double angleBodyCenterX = (Math.PI / 2.0) - Math.atan2(totalX, totalY);

        double rollZ = Math.tan(rotInput.getZ() * Math.PI / 180.0) * totalX;
        double pitchZ = Math.tan(rotInput.getX() * Math.PI / 180.0) * totalY;

        double IKX = Math.cos(angleBodyCenterX + (rotInput.getY() * Math.PI / 180.0)) * distBodyCenterFeet - totalX;
        double IKY = (Math.sin(angleBodyCenterX + (rotInput.getY() * Math.PI / 180.0)) * distBodyCenterFeet) - totalY;
        double IKZ = rollZ + pitchZ;

//        LOG.info("Calc Body IK Leg totalY: {} totalX: {} disBodyCenter: {} angleX: {} rollZ: {} pitchZ: {} IKX: {} IKY: {} IKZ: {}", totalY, totalX, distBodyCenterFeet, angleBodyCenterX, rollZ, pitchZ, IKX, IKY, IKZ);

        return new PosData(IKX, IKY, IKZ);
    }

    private static AngleData createIKLeg(Leg leg, HexRobotBaseData baseData, PosData bodyIK, PosData dirInput) {
        double cLength = baseData.getCoxaLength();
        double fLength = baseData.getFemurLength();
        double tLength = baseData.getTibiaLength();


        double newPosX = leg.getInitialPosition().getX() + dirInput.getX() + bodyIK.getX();
        double newPosZ = leg.getInitialPosition().getZ() + dirInput.getZ() + bodyIK.getZ();
        double newPosY = leg.getInitialPosition().getY() + dirInput.getY() + bodyIK.getY();

//        LOG.info("POSX: {} POSZ: {} POSY: {}", newPosX, newPosZ, newPosY);
//
        double coxaFeetDist = Math.sqrt(powOf2(newPosX) + powOf2(newPosY));
        double iksw = Math.sqrt(powOf2((coxaFeetDist - cLength)) + powOf2(newPosZ));

        double ika1 = Math.atan((coxaFeetDist - cLength) / newPosZ);
        double ika2 = Math.acos((powOf2(tLength) - powOf2(fLength) - powOf2(iksw)) / (-2 * iksw * fLength));
        double tangle = Math.acos((powOf2(iksw) - powOf2(tLength) - powOf2(fLength)) / (-2 * fLength * tLength));

//        LOG.info("feetDist: {} iksw: {} ika1: {} ika2: {} tangle: {}", coxaFeetDist, iksw, ika1, ika2, tangle);
//
        double tibiaAngle = 90.0 - tangle * 180.0 / Math.PI;
        double femurAngle = 90.0 - (ika1 + ika2) * 180.0/Math.PI;
        double coxaAngle = 90.0 - Math.atan2(newPosX, newPosY) * 180.0/Math.PI;

//        LOG.info("Tibia Angle: {} Femur Angle: {} Coxa Angle: {}", tibiaAngle, femurAngle, coxaAngle);
//
//
        return new AngleData(convertToDynamixelPosition(coxaAngle  + leg.getCoxaOffset(), leg.getDirMod()),
                convertToDynamixelPosition(femurAngle - 30, leg.getDirMod()),
                    convertToDynamixelPosition(tibiaAngle + 90, leg.getDirMod()));
    }

    private static double convertToDynamixelPosition(double angle, double mod) {
        double r = (mod * (angle * DYNA_DEGREE_CONVERSION)) + 512;
//        LOG.debug("Convert: {} with mod: {} to value: {} rsult: {}", angle, mod, (angle * DYNA_DEGREE_CONVERSION), r);
        return r;
    }

    private static Double powOf2(Double v) {
        return Math.pow(v, 2);
    }
}
