# Dynamixel Servo Library
This is a Java Robotics Framework with specific support for Dynamixel Servos.

Currently this framework is tested with two different Dynamixel Servo's namely:
* Dynamixel AX-12 (using protocol 1.0)
* Dynamixel XL430-W250T (using protocol 2.0)
 

Also is a built-in Robotis motion file converter that allows you to execute motion files created 
by RoboPlus Motion editor in the controller code.

Next to this there is a web console that allows controlling the servos from a nice WEB-UI and a REST API to control
via direct alls if so desired.


# Using the Dynamixel Web Console
The web console for controlling the servo's is packaged as a spring boot application, the best way to start this is to run the following command:

```
mvn -f dynamixel-web/pom.xml spring-boot:run
```

If no parameters are set the application will not detect your servo's, look below for the configuration settings.

## Configuring the Servo's

In order for the Dynamixel servo's to be detected please set the following properties:
```
#COM port, this is the mac, this string varies per OS, default not configured
-Ddynamixel.port=/dev/tty.usbmodem1441
#Baud rate at which the servo's talk, on OSX this is limited to max 500000, default is 57600, default dynamixels are configured for 1Mbit, 
#you need to reconfigure them in that case using the Dynamixel Manager from Robotis
-Ddynamixel.baudrate=57600
#Enable the Dynamixel V2 protocol, cannot be mixed with V1 is needed for newer Dynamixel Servos, default is disabled
-Dprotocol.v2.enabled=false
```

The full command could look like this
```
mvn -f dynamixel-web/pom.xml spring-boot:run -Ddynamixel.port=/dev/tty.usbmodem1441 -Ddynamixel.baudrate=57600 -Dprotocol.v2.enabled=false 
```

# Usage
In order to use the library in your own project, please clone the repository and make sure you build it locally. The main dependency will be the dynamixel-core library for maven users.

As this is a spring based project, please import the ```DynamixelConfiguration``` bean in your project, and ensure above settings are specified as either startup parameters, 
environment var or in a property file.

Note: Once the library is more mature we will provide artefacts via Maven Central, for now it all depends on local builds.

## Build Requirements
* Java >= 8
* Maven >= 3.1.x


