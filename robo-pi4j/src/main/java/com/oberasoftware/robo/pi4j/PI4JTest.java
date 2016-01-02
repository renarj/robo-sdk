package com.oberasoftware.robo.pi4j;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Renze de Vries
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class PI4JTest {
    private static final Logger LOG = LoggerFactory.getLogger(PI4JTest.class);

    private final byte address1;
    private final byte address2;
    private I2CDevice device1;
    private I2CDevice device2;

    private boolean isConnected;
    private byte config1 = (byte)0x9C;  // PGAx1, 18 bit, continuous conversion, channel 1
    private byte currentchannel1 = 0;  // channel variable for ADC 1
    private byte config2 = (byte)0x9C;  // PGAx1, 18 bit, continuous-shot conversion, channel 1
    private byte currentchannel2 = 0;  // channel variable for ADC 2
    private byte bitrate = 18;  // current bit rate
    private byte conversionmode = 1; // Conversion Mode
    private double pga = 0.5;  // current PGA setting
    private double lsb = 0.0000078125;  // default LSB value for 18 bit
    private boolean signbit = false;

//    Byte[] __adcreading = { 0, 0, 0, 0 };

    public PI4JTest(byte i2caddress1, byte i2caddress2) {
        address1 = i2caddress1;
        address2 = i2caddress2;
        isConnected = false;
    }

    public void connect() {
        try {
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            device1 = bus.getDevice(address1);
            device2 = bus.getDevice(address2);

            // check if the i2c busses are not null
            if ((device1 != null) && (device2 != null)) {
                // set the initial bit rate and trigger a Connected event handler
                isConnected = true;
                SetBitRate(bitrate);
            } else {
                isConnected = false;
            }
        } catch (Exception e) {
            isConnected = false;
            LOG.error("", e);
        }
    }

    public void SetBitRate(int rate) {
        switch (rate) {
            case 12:
                config1 = UpdateByte(config1, 2, false);
                config1 = UpdateByte(config1, 3, false);
                config2 = UpdateByte(config2, 2, false);
                config2 = UpdateByte(config2, 3, false);
                bitrate = 12;
                lsb = 0.0005;
                break;
            case 14:
                config1 = UpdateByte(config1, 2, true);
                config1 = UpdateByte(config1, 3, false);
                config2 = UpdateByte(config2, 2, true);
                config2 = UpdateByte(config2, 3, false);
                bitrate = 14;
                lsb = 0.000125;
                break;
            case 16:
                config1 = UpdateByte(config1, 2, false);
                config1 = UpdateByte(config1, 3, true);
                config2 = UpdateByte(config2, 2, false);
                config2 = UpdateByte(config2, 3, true);
                bitrate = 16;
                lsb = 0.00003125;
                break;
            case 18:
                config1 = UpdateByte(config1, 2, true);
                config1 = UpdateByte(config1, 3, true);
                config2 = UpdateByte(config2, 2, true);
                config2 = UpdateByte(config2, 3, true);
                bitrate = 18;
                lsb = 0.0000078125;
                break;
            default:
                throw new RuntimeException();
        }
        WriteI2CSingleByte(device1, config1);
        WriteI2CSingleByte(device2, config2);
    }

    private void SetChannel(byte channel) {
            /* Checks to see if the selected channel is already the current channel.  
            If not then update the appropriate config to the new channel settings. */

        if ((channel < 5) && (channel != currentchannel1)) {
            switch (channel) {
                case 1:
                    config1 = UpdateByte(config1, 5, false);
                    config1 = UpdateByte(config1, 6, false);
                    currentchannel1 = 1;
                    break;
                case 2:
                    config1 = UpdateByte(config1, 5, true);
                    config1 = UpdateByte(config1, 6, false);
                    currentchannel1 = 2;
                    break;
                case 3:
                    config1 = UpdateByte(config1, 5, false);
                    config1 = UpdateByte(config1, 6, true);
                    currentchannel1 = 3;
                    break;
                case 4:
                    config1 = UpdateByte(config1, 5, true);
                    config1 = UpdateByte(config1, 6, true);
                    currentchannel1 = 4;
                    break;
            }
        } else if ((channel >= 5 && channel <= 8) && (channel != currentchannel2)) {
            switch (channel) {
                case 5:
                    config2 = UpdateByte(config2, 5, false);
                    config2 = UpdateByte(config2, 6, false);
                    currentchannel2 = 5;
                    break;
                case 6:
                    config2 = UpdateByte(config2, 5, true);
                    config2 = UpdateByte(config2, 6, false);
                    currentchannel2 = 6;
                    break;
                case 7:
                    config2 = UpdateByte(config2, 5, false);
                    config2 = UpdateByte(config2, 6, true);
                    currentchannel2 = 7;
                    break;
                case 8:
                    config2 = UpdateByte(config2, 5, true);
                    config2 = UpdateByte(config2, 6, true);
                    currentchannel2 = 8;
                    break;
            }
        }
    }

    public double ReadVoltage(byte channel) {
        int raw = ReadRaw(channel); // get the raw value
        // check to see if the sign bit is present, if it is then the voltage is negative and can be ignored.
        if (signbit) {
            return -1;  // returned a negative voltage so return 0
        } else {
            double voltage = (raw * (lsb / pga)) * 2.471; // calculate the voltage and return it
            return voltage;
        }
    }

    public int ReadRaw(byte channel)
    {
        // variables for storing the raw bytes from the ADC
        byte h = 0;
        byte l = 0;
        byte m = 0;
        byte s = 0;
        byte config = 0;

        int t = 0;
        signbit = false;

        // create a new instance of the I2C device
        I2CDevice bus;

        SetChannel(channel);

        // get the configuration and i2c bus for the selected channel
        if (channel < 5)
        {
            config = config1;
            bus = device1;
        }
        else if (channel >= 5 && channel <= 8)
        {
            config = config2;
            bus = device2;
        }
        else
        {
            throw new RuntimeException();
        }

        // if the conversion mode is set to one-shot update the ready bit to 1
        if (conversionmode == 0)
        {
            config = UpdateByte(config, 7, true);
            WriteI2CByte(bus, config, (byte)0x00);
            config = UpdateByte(config, 7, false);
        }

        // keep reading the ADC data until the conversion result is ready
        int timeout = 1000; // number of reads before a timeout occurs
        int x = 0;
        do
        {
            if (bitrate == 18)
            {
                byte[] adcreading = ReadI2CBlockData(bus, config, (byte)4);
                h = adcreading[0];
                m = adcreading[1];
                l = adcreading[2];
                s = adcreading[3];
            }
            else
            {
                byte[] adcreading = ReadI2CBlockData(bus, config, (byte)3);
                h = adcreading[0];
                m = adcreading[1];
                s = adcreading[2];
            }

            // check bit 7 of s to see if the conversion result is ready
            if (!CheckBit(s, (byte)7))
            {
                break;
            }

            if (x > timeout)
            {
                // timeout occurred
                throw new RuntimeException();
            }

            x++;
        } while (true);

        // extract the returned bytes and combine in the correct order
        switch (bitrate)
        {
            case 18:

                t = ((h & 3) << 16) | (m << 8) | l;
                signbit = CheckIntBit(t, 17);
                if (signbit)
                {
                    t = UpdateInt(t, 17, false);
                }
                break;
            case 16:
                t = (h << 8) | m;
                signbit = CheckIntBit(t, 15);
                if (signbit)
                {
                    t = UpdateInt(t, 15, false);
                }
                break;
            case 14:

                t = ((h & 63) << 8) | m;
                signbit = CheckIntBit(t, 13);
                if (signbit)
                {
                    t = UpdateInt(t, 13, false);
                }
                break;
            case 12:
                t = ((h & 15) << 8) | m;
                signbit = CheckIntBit(t, 11);
                if (signbit)
                {
                    t = UpdateInt(t, 11, false);
                }
                break;
            default:
                throw new RuntimeException();
        }

        return t;
    }

    public void SetPGA(int gain)
    {
        // update the configs with the new gain settings
        switch (gain)
        {
            case 1:
                config1 = UpdateByte(config1, 0, false);
                config1 = UpdateByte(config1, 1, false);
                config2 = UpdateByte(config2, 0, false);
                config2 = UpdateByte(config2, 1, false);
                pga = 0.5;
                break;
            case 2:
                config1 = UpdateByte(config1, 0, true);
                config1 = UpdateByte(config1, 1, false);
                config2 = UpdateByte(config2, 0, true);
                config2 = UpdateByte(config2, 1, false);
                pga = 1;
                break;
            case 4:
                config1 = UpdateByte(config1, 0, false);
                config1 = UpdateByte(config1, 1, true);
                config2 = UpdateByte(config2, 0, false);
                config2 = UpdateByte(config2, 1, true);
                pga = 2;
                break;
            case 8:
                config1 = UpdateByte(config1, 0, true);
                config1 = UpdateByte(config1, 1, true);
                config2 = UpdateByte(config2, 0, true);
                config2 = UpdateByte(config2, 1, true);
                pga = 4;
                break;
            default:
                throw new RuntimeException();
        }
        WriteI2CSingleByte(device1, config1);
        WriteI2CSingleByte(device2, config2);
    }    
    
    public static void main(String[] args) {
        try {
            PI4JTest test = new PI4JTest((byte)0x68, (byte)0x69);
            test.connect();
            test.SetPGA(1);
            test.SetBitRate(16);

            LOG.info("Connected");
            while(true) {
                double voltage = test.ReadVoltage((byte) 0x01);
                if(voltage > 0) {
                    LOG.info("Voltage: {}", voltage);
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    private void WriteI2CSingleByte(I2CDevice bus, byte value) {
        byte[] writeBuffer = new byte[] { value };
        try {
            bus.write(writeBuffer, 0, 1);
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    private void WriteI2CByte(I2CDevice bus, byte register, byte value) {
        byte[] writeBuffer = new byte[] { register, value };
        try {
            bus.write(writeBuffer, 0, 2);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private byte[] ReadI2CBlockData(I2CDevice bus, byte register, byte bytesToReturn) {
        try {
            byte[] readBuffer = new byte[] { register };
            byte[] returnValue = new byte[bytesToReturn];

            bus.write(readBuffer, 0, 1);
            bus.read(returnValue, 0, bytesToReturn);

            return returnValue;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private byte UpdateByte(byte value, int position, boolean bitstate) {
        if (bitstate) {
            //left-shift 1, then bitwise OR
            return (byte) (value | (1 << position));
        } else {
            //left-shift 1, then take complement, then bitwise AND
            return (byte) (value & ~(1 << position));
        }
    }

    private int UpdateInt(int value, int position, boolean bitstate) {
        if (bitstate) {
            //left-shift 1, then bitwise OR
            return value | (1 << position);
        } else {
            //left-shift 1, then take complement, then bitwise AND
            return value & ~(1 << position);
        }
    }

    /// <summary>
    /// Checks the value of a single bit within a byte
    /// </summary>
    /// <param name="value">The value to query</param>
    /// <param name="position">The bit position within the byte</param>
    /// <returns></returns>
    private boolean CheckBit(byte value, int position) {
        // internal method for reading the value of a single bit within a byte
        return (value & (1 << position)) != 0;
    }

    private boolean CheckIntBit(int value, int position) {
        // internal method for reading the value of a single bit within a byte
        return (value & (1 << position)) != 0;
    }
}
