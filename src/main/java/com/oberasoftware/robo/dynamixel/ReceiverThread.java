package com.oberasoftware.robo.dynamixel;

import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author Renze de Vries
 */
public class ReceiverThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ReceiverThread.class);

    private final InputStream inputStream;

    public ReceiverThread(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        LOG.info("Thread started");

        while(!Thread.currentThread().isInterrupted()) {
            try {
                int read = inputStream.read();

                LOG.debug("Received a byte: {} hex: {}", read, Integer.toHexString(read));

                if(read == -1) {
                    Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
                }

            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        LOG.info("Thread finished");
    }
}
