package com.oberasoftware.home.core.mqtt;

import io.moquette.spi.security.IAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class TestAuthenticationClass implements IAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(TestAuthenticationClass.class);

    @Override
    public boolean checkValid(String s, byte[] bytes) {
        LOG.info("Checking authentication for user: {} and password: {}", s, new String(bytes));
        return s.equals("renze") && new String(bytes).equals("mySecret");
    }
}
