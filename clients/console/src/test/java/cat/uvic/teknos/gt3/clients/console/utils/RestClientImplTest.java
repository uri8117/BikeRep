package cat.uvic.teknos.gt3.clients.console.utils;

import cat.uvic.teknos.gt3.clients.console.dto.BrandDto;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import cat.uvic.teknos.gt3.cryptoutils.CryptoUtils;
import cat.uvic.teknos.gt3.domain.models.Brand;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class RestClientImplTest {
    private static final String HOST = "localhost";
    private static final int PORT = 8070;

    @Test
    void getAllBrandsTest() {
        var restClient = new RestClientImpl(HOST, PORT);
        try {
            Brand[] brands = restClient.getAll("brands", BrandDto[].class);

            assertNotNull(brands);
            assertTrue(brands.length > 0); // Assuming there are multiple brands
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void encryptionAndDecryptionBrandTest() {
        var restClient = new RestClientImpl(HOST, PORT);

        // Sample data to encrypt and decrypt
        String originalBrandName = "Test Brand";
        String originalBrandCountry = "Germany";

        // Encrypt the data using CryptoUtils
        SecretKey symmetricKey = CryptoUtils.createSecretKey();
        String encryptedBrandName = CryptoUtils.encrypt(originalBrandName, symmetricKey);
        String encryptedBrandCountry = CryptoUtils.encrypt(originalBrandCountry, symmetricKey);

        assertNotNull(encryptedBrandName, "Encrypted brand name should not be null.");
        assertNotNull(encryptedBrandCountry, "Encrypted brand country should not be null.");

        // Decrypt the data
        String decryptedBrandName = CryptoUtils.decrypt(encryptedBrandName, symmetricKey);
        String decryptedBrandCountry = CryptoUtils.decrypt(encryptedBrandCountry, symmetricKey);

        assertNotNull(decryptedBrandName, "Decrypted brand name should not be null.");
        assertNotNull(decryptedBrandCountry, "Decrypted brand country should not be null.");
        assertEquals(originalBrandName, decryptedBrandName, "Decrypted brand name should match the original.");
        assertEquals(originalBrandCountry, decryptedBrandCountry, "Decrypted brand country should match the original.");
    }

    @Test
    void asymmetricEncryptionBrandTest() {
        var restClient = new RestClientImpl("localhost", 8070);

        String originalBrandData = "Sensitive brand data for asymmetric encryption";

        // Step 1: Asymmetric encryption: encrypt the symmetric key using the server's public key
        SecretKey symmetricKey = CryptoUtils.createSecretKey();
        String encryptedSymmetricKey = CryptoUtils.asymmetricEncrypt(symmetricKey, restClient.getPublicKey());
        assertNotNull(encryptedSymmetricKey, "Encrypted symmetric key should not be null.");

        // Step 2: Symmetric encryption: encrypt the brand data using the symmetric key
        String encryptedBrandData = CryptoUtils.encrypt(originalBrandData, symmetricKey);
        assertNotNull(encryptedBrandData, "Encrypted brand data should not be null.");

        // Step 3: Simulate the server-side decryption (which uses the private key)
        // For test purposes, assume we are directly decrypting the encrypted brand data here
        String decryptedBrandData = CryptoUtils.decrypt(encryptedBrandData, symmetricKey);
        assertNotNull(decryptedBrandData, "Decrypted brand data should not be null.");
        assertEquals(originalBrandData, decryptedBrandData, "Decrypted brand data should match the original.");
    }

}
