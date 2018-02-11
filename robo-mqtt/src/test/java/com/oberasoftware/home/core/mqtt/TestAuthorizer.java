package com.oberasoftware.home.core.mqtt;

import io.moquette.spi.security.IAuthorizator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class TestAuthorizer implements IAuthorizator {
    private static final Logger LOG = LoggerFactory.getLogger(TestAuthorizer.class);

    @Override
    public boolean canWrite(String s, String s1, String s2) {
        LOG.info("Can write: {}, {}, {}", s, s1, s2);
        return s.startsWith("/states") && s1.equals("piet");
    }

    @Override
    public boolean canRead(String s, String s1, String s2) {
        LOG.info("Can read: {}, {}, {}", s, s1, s2);
        return true;
    }
}
