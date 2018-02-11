package com.oberasoftware.robo.dynamixel;

/**
 * @author Renze de Vries
 */
public enum DynamixelAddress {
    MODEL_NUMBER_L(0x00),
    MODEL_NUMBER_H(0x01),
    VERSION_FIRMWARE(0x02),
    ID(0x03),
    BAUD_RATE(0x04),
    RETURN_DELAY_TIME(0x05),
    CW_ANGLE_LIMIT_L(0x06),
    CW_ANGLE_LIMIT_H(0x07),
    CCW_ANGLE_LIMIT_L(0x08),
    CCW_ANGLE_LIMIT_H(0x09),
    ALARM_LED(0x11),
    ALARM_SHUTDOWN(0x12),
    TORGUE_ENABLE(0x18),
    LED(0x19),
    MOVING_SPEED_L(0x20),
    MOVING_SPEED_H(0x21),
    TORGUE_LIMIT_L(0x22),
    TORGUE_LIMIT_H(0x23),
    GOAL_POSITION_L(0x1E),
    GOAL_POSITION_H(0x1F),
    PRESENT_POSITION_L(0x24),
    PRESENT_POSITION_H(0x25),
    PRESENT_SPEED_L(0x26),
    PRESENT_SPEED_H(0x27),
    CURRENT_VOLTAGE(0x2A),
    PRESENT_TEMPERATURE(0x2B);


    private int address;

    DynamixelAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "DynamixelAddress{" +
                "address=" + address +
                '}';
    }
}
