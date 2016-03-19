package com.oberasoftware.robo.dynamixel;

import com.google.common.util.concurrent.Uninterruptibles;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.oberasoftware.robo.dynamixel.DynamixelCommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
@Component
public class SerialDynamixelConnector implements DynamixelConnector {
    private static final Logger LOG = LoggerFactory.getLogger(SerialDynamixelConnector.class);

    private SerialPort serialPort;

    private String portName;

    private List<Byte> buffer = new CopyOnWriteArrayList<>();

    @Override
    public void connect(String portName) {
        this.portName = portName;
        LOG.info("Connecting to serial port: {}", portName);

        serialPort = new SerialPort(portName);
        try {
            boolean opened = serialPort.openPort();
            LOG.debug("Port: {} opened: {}", portName, opened);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setParams(1000000, 8, 1, 0);
            serialPort.purgePort(SerialPort.PURGE_TXCLEAR);

            if(opened) {
                LOG.debug("Registered event listener");
                serialPort.addEventListener(new DynamixelSerialEventListener());
            }
        } catch (SerialPortException e) {
            LOG.error("Unable to open serial port", e);
        }
    }

    @Override
    public void disconnect() {
        LOG.info("Disconnecting from serial port: {}", portName);

        try {
            boolean closed = serialPort.closePort();
            LOG.info("Closed port: {} successfully: {}", portName, closed);
        } catch (SerialPortException e) {
            LOG.error("Unable to close port to dynamixel controller", e);
        }
    }

    @Override
    public synchronized byte[] sendAndReceive(byte[] bytes) {
        LOG.debug("Sending message: {}", bb2hex(bytes));

        try {
            boolean writeSuccess = serialPort.writeBytes(bytes);
            if(writeSuccess) {
                Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
                return readBytes();
            } else {
                LOG.warn("Write was not successful");
            }
        } catch (SerialPortException e) {
            LOG.error("Unable to write message to dynamixel controller", e);
        }

        return new byte[0];
    }

    private byte[] readBytes() {
        List<Byte> copyBytes = new ArrayList<>(buffer);
        buffer.clear();

        byte[] bu = new byte[copyBytes.size()];
        for(int i=0; i<copyBytes.size(); i++) {
            bu[i] = copyBytes.get(i);
        }
        return bu;
    }

    private class DynamixelSerialEventListener implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
//            LOG.debug("Bytes in buffer: {}", serialPortEvent.getEventValue());
            try {
                byte[] readBytes = serialPort.readBytes();
                for(byte b : readBytes) {
                    buffer.add(b);
                }
            } catch (SerialPortException e) {
                LOG.error("", e);
            }
        }
    }
}
