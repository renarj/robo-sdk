package com.oberasoftware.robo.dynamixel;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * @author Renze de Vries
 */
public class Dynamixel {
    private static final Logger LOG = LoggerFactory.getLogger(Dynamixel.class);

    public static void main(String[] args) {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1411");
            CommPort commPort = portIdentifier.open("roboport", 2000);



            SerialPort serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); //115200
//            serialPort.enableReceiveThreshold(1);
            serialPort.enableReceiveTimeout(1000);

            ReceiverThread receiverThread = new ReceiverThread(serialPort.getInputStream());
            new Thread(receiverThread).start();

            LOG.info("Going to send a control byte");
            OutputStream outputStream = serialPort.getOutputStream();

                byte[] buffer = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte)0x06, 0x04, 0x03, 0x19, 0x01, (byte) 0xD8};


//            byte[] buffer = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte)0xFD, 0x00,
//
//                    (byte)0x06, //id
//
//                    0x04, 0x03, 0x19, 0x01, (byte) 0xD8};
                outputStream.write(buffer);
            outputStream.flush();

                LOG.info("Finished writing stuff");


        } catch(Exception e) {
            LOG.error("", e);
        }


    }
}
