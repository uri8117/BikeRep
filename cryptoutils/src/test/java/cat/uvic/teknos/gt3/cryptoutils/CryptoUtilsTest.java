package cat.uvic.teknos.gt3.cryptoutils;

import cat.uvic.teknos.gt3.cryptoutils.CryptoUtils;
import org.junit.jupiter.api.Test;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void getHash() {
        var text = "Test Base";
        var base64Test = "+7GOluCsemmPk8tnVO8eQwM3exipYfoqCwSD+oxgSLM=";
        System.out.println(CryptoUtils.getHash(text));

        assertEquals(base64Test, CryptoUtils.getHash(text));

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
        var secretKeyBase84 = "jaruKzlE7xerbNSjxiVjZtuAeYWrcyMGsA8TaTqZ8AM=";

        var secretKey = CryptoUtils.decodeSecretKey(secretKeyBase84);

        assertNotNull(secretKey);
        assertEquals("AES", secretKey.getAlgorithm());
    }

    @Test
    void encrypt() {
        String plainText = "Hellou";
        var secretKey = CryptoUtils.createSecretKey();
        String encryptedText = CryptoUtils.encrypt(plainText, secretKey);
        assertNotNull(encryptedText);
        assertNotEquals(plainText, encryptedText);

        String decryptedText = CryptoUtils.decrypt(encryptedText, secretKey);
        assertEquals(plainText, decryptedText);
    }

    @Test
    void decrypt() {
        String plainText = "Hellou";
        var secretKey = CryptoUtils.createSecretKey();
        String encryptedText = CryptoUtils.encrypt(plainText, secretKey);

        String decryptedText = CryptoUtils.decrypt(encryptedText, secretKey);
        assertNotNull(decryptedText);
        assertEquals(plainText, decryptedText);
    }

    @Test
    void asymmetricEncrypt() throws NoSuchAlgorithmException {
        /*String plainText = "Hellou";*/
        var key = CryptoUtils.createSecretKey();

        var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        var keyPair = keyPairGenerator.generateKeyPair();

        String encryptedText = CryptoUtils.asymmetricEncrypt(key, keyPair.getPublic());
        assertNotNull(encryptedText);
        assertNotEquals(CryptoUtils.toBase64(key.getEncoded()), encryptedText);

        var decryptedText = CryptoUtils.asymmetricDecrypt(encryptedText, keyPair.getPrivate());
        assertNotNull(decryptedText);
        assertEquals("AES", decryptedText.getAlgorithm());
    }

    @Test
    void asymmetricDecrypt() throws NoSuchAlgorithmException {
        var key = CryptoUtils.createSecretKey();

        var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        var keyPair = keyPairGenerator.generateKeyPair();

        String encryptedText = CryptoUtils.asymmetricEncrypt(key, keyPair.getPublic());
        assertNotNull(encryptedText);

        var decryptedText = CryptoUtils.asymmetricDecrypt(encryptedText, keyPair.getPrivate());
        assertNotNull(decryptedText);
        assertEquals("AES", decryptedText.getAlgorithm());
    }
}