package cat.uvic.teknos.coursemanagement.cryptoutils;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void getHash() {
        var text = "Some text...";
        var base64Text = "quonJ6BjRSC1DBOGuBWNdqixj8z20nuP+QH7cVvp7PI=";

        assertEquals(base64Text, CryptoUtils.getHash(text));
    }

    @Test
    void createSecretKey() {
        var secretKey = CryptoUtils.createSecretKey();

        assertNotNull(secretKey);

        var bytes = secretKey.getEncoded();
        System.out.println(CryptoUtils.toBase64(bytes));
    }

    @Test
    void decodeSecretKey() {
        var secretKeyBase64 = "jaruKzlE7xerbNSjxiVjZtuAeYWrcyMGsA8TaTqZ8AM=";

        var secretKey = CryptoUtils.decodeSecretKey(secretKeyBase64);

        assertNotNull(secretKey);
        assertEquals("AES", secretKey.getAlgorithm());
    }

    @Test
    void encrypt() {
        var text = "Confidential data";
        var secretKey = CryptoUtils.createSecretKey();

        var encryptedData = CryptoUtils.encrypt(text, secretKey);

        assertNotNull(encryptedData);
        assertNotEquals(text, encryptedData);
    }

    @Test
    void decrypt() {
        var text = "Confidential data";
        var secretKey = CryptoUtils.createSecretKey();

        var encryptedData = CryptoUtils.encrypt(text, secretKey);
        var decryptedData = CryptoUtils.decrypt(encryptedData, secretKey);

        assertNotNull(decryptedData);
        assertEquals(text, decryptedData);
    }

    @Test
    void asymmetricEncrypt() {
        var text = "Sensitive information";
        var keyPair = CryptoUtils.generateKeyPair();

        var encryptedData = CryptoUtils.asymmetricEncrypt(text, keyPair.getPublic());

        assertNotNull(encryptedData);
        assertNotEquals(text, encryptedData);
    }

    @Test
    void asymmetricDecrypt() {
        var text = "Sensitive information";
        var keyPair = CryptoUtils.generateKeyPair();

        var encryptedData = CryptoUtils.asymmetricEncrypt(text, keyPair.getPublic());
        var decryptedData = CryptoUtils.asymmetricDecrypt(encryptedData, keyPair.getPrivate());

        assertNotNull(decryptedData);
        assertEquals(text, decryptedData);
    }
}
