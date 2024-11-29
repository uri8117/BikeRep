package cat.uvic.teknos.coursemanagement.cryptoutils;

import cat.uvic.teknos.coursemanagement.cryptoutils.exeptions.CryptoException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class CryptoUtils {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static String getHash(String text) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(text.getBytes());

            return toBase64(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Error generating hash", e);
        }
    }

    public static SecretKey createSecretKey() {
        try {
            return KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Error creating secret key", e);
        }
    }

    public static SecretKey decodeSecretKey(String base64SecretKey) {
        try {
            var bytes = decoder.decode(base64SecretKey);
            return new SecretKeySpec(bytes, 0, bytes.length, "AES");
        } catch (IllegalArgumentException e) {
            throw new CryptoException("Invalid Base64 key format", e);
        }
    }

    public static String encrypt(String plainText, SecretKey key) {
        try {
            var cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return toBase64(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new CryptoException("Error encrypting data", e);
        }
    }

    public static String decrypt(String encryptedTextBase64, SecretKey key) {
        try {
            var cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(fromBase64(encryptedTextBase64)));
        } catch (Exception e) {
            throw new CryptoException("Error decrypting data", e);
        }
    }

    public static String asymmetricEncrypt(String plainText, PublicKey publicKey) {
        try {
            var cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return toBase64(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new CryptoException("Error encrypting data asymmetrically", e);
        }
    }

    public static String asymmetricDecrypt(String encryptedTextBase64, PrivateKey privateKey) {
        try {
            var cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(fromBase64(encryptedTextBase64)));
        } catch (Exception e) {
            throw new CryptoException("Error decrypting data asymmetrically", e);
        }
    }

    public static KeyPair generateKeyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Error generating RSA key pair", e);
        }
    }

    public static String toBase64(byte[] bytes) {
        return encoder.encodeToString(bytes);
    }

    public static byte[] fromBase64(String base64) {
        try {
            return decoder.decode(base64);
        } catch (IllegalArgumentException e) {
            throw new CryptoException("Invalid Base64 input", e);
        }
    }
}
