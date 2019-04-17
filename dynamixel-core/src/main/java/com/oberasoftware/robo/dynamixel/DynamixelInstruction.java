package com.oberasoftware.robo.dynamixel;

/**
 * @author Renze de Vries
 */
public enum DynamixelInstruction {
    PING(0x01),
    READ_DATA(0x02),
    WRITE_DATA(0x03),
    REG_WRITE(0x04),
    ACTION(0x05),
    RESET(0x06),
    REBOOT(0x08),
    SYNC_WRITE(0x83);

    private int instruction;

    DynamixelInstruction(int instruction) {
        this.instruction = instruction;
    }

    public int getInstruction() {
        return instruction;
    }
}
