package com.oberasoftware.home.util.crypto;

import com.oberasoftware.robo.api.exceptions.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;

/**
 * @author Renze de Vries
 */
public class CryptoFactory {
    private static final Logger LOG = LoggerFactory.getLogger(CryptoFactory.class);
    private static final int STRONG_ENGINE_LIMIT = 256;


    public static CryptoEngine getEngine() {
        boolean limitedEngine = false;
        try {
            limitedEngine = Cipher.getMaxAllowedKeyLength("AES") < STRONG_ENGINE_LIMIT;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("No support for AES encryption", e);
        }

        LOG.debug("Creating crypto engine, can use strong keys: {}", !limitedEngine);
        return limitedEngine ? new BasicCryptoEngine() : new StrongCryptoEngine();
    }

    public static CryptoEngine getEngine(String engineDescriptor) throws SecurityException {
        if(StrongCryptoEngine.STRONG_CRYPTO.equals(engineDescriptor)) {
            return new StrongCryptoEngine();
        } else if(BasicCryptoEngine.BASIC_CRYPTO.equals(engineDescriptor)) {
            return new BasicCryptoEngine();
        } else {
            throw new SecurityException("Unsupported encryption engine: " + engineDescriptor);
        }
    }
}
