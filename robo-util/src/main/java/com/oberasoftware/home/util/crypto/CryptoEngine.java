package com.oberasoftware.home.util.crypto;

import com.oberasoftware.robo.api.exceptions.SecurityException;

/**
 * @author Renze de Vries
 */
public interface CryptoEngine {
    String getDescriptor();

    String encrypt(String salt, String password, String text) throws SecurityException;

    String decrypt(String salt, String password, String encrypted) throws SecurityException;

    String generateSalt() throws SecurityException;

    String hash(String salt, String password) throws SecurityException;
}
