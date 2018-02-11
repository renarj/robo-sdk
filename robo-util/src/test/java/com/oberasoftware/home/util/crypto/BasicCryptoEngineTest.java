package com.oberasoftware.home.util.crypto;

import com.oberasoftware.robo.api.exceptions.SecurityException;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Renze de Vries
 */
@Ignore
public class BasicCryptoEngineTest {
    private static final String TEXT_TO_ENCRYPT = "Text To Encrypt";

    @Test
    public void testEmptyPasswordEncrypt() throws SecurityException {
        BasicCryptoEngine cryptoEngine = new BasicCryptoEngine();
        String salt = cryptoEngine.generateSalt();
        String encrypted = cryptoEngine.encrypt(salt, "", TEXT_TO_ENCRYPT);
        assertThat(cryptoEngine.decrypt(salt, "", encrypted), is("Text To Encrypt"));
    }

    @Test
    public void testPasswordEncrypt() throws SecurityException {
        BasicCryptoEngine cryptoEngine = new BasicCryptoEngine();
        String salt = cryptoEngine.generateSalt();
        String encrypted = cryptoEngine.encrypt(salt, "12345", TEXT_TO_ENCRYPT);
        assertThat(cryptoEngine.decrypt(salt, "12345", encrypted), is("Text To Encrypt"));
    }

    @Test(expected = SecurityException.class)
    public void testInvalidPasswordDecrypt() throws SecurityException {
        BasicCryptoEngine cryptoEngine = new BasicCryptoEngine();
        String salt = cryptoEngine.generateSalt();
        String encrypted = cryptoEngine.encrypt(salt, "1234", TEXT_TO_ENCRYPT);
        cryptoEngine.decrypt(salt, "invalidPass", encrypted);
    }

    @Test
    public void testPasswordHash() throws SecurityException {
        BasicCryptoEngine cryptoEngine = new BasicCryptoEngine();

        String password = "1234";
        String salt = cryptoEngine.generateSalt();

        String hash = cryptoEngine.hash(salt, password);
        assertThat(hash, not(equalTo("")));
        assertThat(hash, not(equalTo(password)));

        assertThat(cryptoEngine.hash(salt, password), is(hash));

        assertThat(cryptoEngine.hash(cryptoEngine.generateSalt(), password), not(equalTo(hash)));
    }
}
