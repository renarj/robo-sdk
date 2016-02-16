# Dynamixel Servo Library
This is a Java based Dynamixel Servo library that allows you to control the movement of the 
Dynamixel Servo's. Also is a built-in Robotis motion file converter that allows you to 
execute motion files created by RoboPlus Motion editor in the controller code.

# Servo Test sample Code
```
@SpringBootApplication
@Import({DynamixelConfiguration.class, BaseConfiguration.class})
public class DynamixelTest {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelTest.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DynamixelTest.class, args);

        RobotController controller = context.getBean(RobotController.class);
        MotionConverter motionConverter = context.getBean(MotionConverter.class);
        List<Motion> motions = motionConverter.loadMotions("/bio_prm_kingspider_en.mtn");
        Map<String, Motion> motionMap = motions.stream().collect(Collectors.toMap(Motion::getName, m -> m));

        boolean s = controller.initialize();
        if(s) {
            LOG.info("Controller succesfully initialized");
        } else {
            LOG.error("Controller could not initialize");
            System.exit(-1);
        }

        List<Servo> servoList = controller.getServos();
        LOG.debug("Found servos: {}", servoList);

        LOG.debug("Enabling torgue");
        servoList.forEach(Servo::enableTorgue);

        LOG.debug("Readying the robot");
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        controller.executeMotion(motionMap.get("Ready"));

        LOG.debug("Doing a short walk in a bit");
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        controller.executeMotion(motionMap.get("Forward walk"), 4);

        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        controller.executeMotion(motionMap.get("Ready"));

        //Create a motion with two steps, containing a few servo's and their goal positions
        Motion motion = MotionBuilder.create("MoveServo").addStep(StepBuilder.create(1000)
                .servo("1", 500)
                .servo("10", 190).build())
                .addStep(StepBuilder.create(500)
                .servo("1", 550)
                .servo("10", 200)
                .servo("12", 500).build())
            .build();
        controller.executeMotion(motion);

        LOG.debug("Waiting for program end");
        Uninterruptibles.sleepUninterruptibly(60, TimeUnit.SECONDS);

        System.exit(0);
    }
}
```

# Usage
In order to use the library please clone the repository and make sure you build it locally. Once the library is 
more mature we will provide artefacts via Maven Central.

## Build Requirements
* Java >= 8
* Maven >= 3.1.x


# OData Functions

```
curl -X GET http://robomax:8080/robot.svc/Motions/Oberasoftware.Robot.Execute(motion='Stand%20up',repeats=0)
```