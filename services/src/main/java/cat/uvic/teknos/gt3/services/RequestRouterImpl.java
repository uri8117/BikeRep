package cat.uvic.teknos.gt3.services;

import cat.uvic.teknos.gt3.services.controllers.Controller;
import cat.uvic.teknos.gt3.services.exception.ResourceNotFoundException;
import cat.uvic.teknos.gt3.services.exception.ServerErrorException;
import cat.uvic.teknos.gt3.cryptoutils.CryptoUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;

public class RequestRouterImpl implements RequestRouter {
    private static RawHttp rawHttp = new RawHttp();
    private final Map<String, Controller> controllers;
    private final PrivateKey privateKey;

    public RequestRouterImpl(Map<String, Controller> controllers) {
        this.controllers = controllers;
        this.privateKey = loadPrivateKey();
    }

    // Carregar la clau privada des d'un KeyStore
    private PrivateKey loadPrivateKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(RequestRouterImpl.class.getResourceAsStream("/server.p12"), "Teknos01.".toCharArray());
            return (PrivateKey) keyStore.getKey("server", "Teknos01.".toCharArray());
        } catch (KeyStoreException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    @Override
    public RawHttpResponse<?> execRequest(RawHttpRequest request) {
        try {
            System.out.println("Processing request: " + request.getMethod() + " " + request.getUri().getPath());

            // Desencriptar la clau simètrica amb la clau privada
            String encryptedKeyBase64 = request.getHeaders().get("X-Symmetric-Key").stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No Encrypted Key Found"));
            SecretKey symmetricKey = CryptoUtils.asymmetricDecrypt(encryptedKeyBase64, privateKey);
            System.out.println("Symmetric key decrypted successfully");

            // Desencriptar el cos de la petició
            String decryptedBody = "";
            if (request.getBody().isPresent()) {
                String encryptedBody = request.getBody().get().decodeBodyToString(Charset.defaultCharset());
                if (!encryptedBody.isEmpty()) {
                    decryptedBody = CryptoUtils.decrypt(encryptedBody, symmetricKey);
                }
            }

            // Processar la petició
            var path = request.getUri().getPath();
            var method = request.getMethod();
            var pathParts = path.split("/");

            String responseJsonBody = handleRequest(method, pathParts, decryptedBody);

            // Si no hi ha resposta, retornar un array buit
            if (responseJsonBody == null) {
                responseJsonBody = "[]";
            }

            // Encriptar la resposta amb la clau simètrica
            String encryptedResponse = CryptoUtils.encrypt(responseJsonBody, symmetricKey);
            String responseHash = CryptoUtils.getHash(encryptedResponse);

            // Retornar la resposta encriptada
            return rawHttp.parseResponse(
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: " + encryptedResponse.getBytes(Charset.defaultCharset()).length + "\r\n" +
                            "X-Body-Hash: " + responseHash + "\r\n" +
                            "\r\n" +
                            encryptedResponse
            );

        } catch (Exception e) {
            System.out.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            return rawHttp.parseResponse("HTTP/1.1 500 Internal Server Error\r\n\r\n" + e.getMessage());
        }
    }

    // Gestió de la petició segons el mètode i les parts de la ruta
    private String handleRequest(String method, String[] pathParts, String decryptedBody) throws Exception {
        var controller = controllers.get(pathParts[1]);

        if (controller == null) {
            throw new ResourceNotFoundException("Controller not found: " + pathParts[1]);
        }

        switch (method) {
            case "POST":
                controller.post(decryptedBody);
                return "{\"message\": \"Resource created successfully.\"}";

            case "GET":
                if (pathParts.length == 2) {
                    return controller.get();
                } else if (pathParts.length == 3) {
                    return controller.get(Integer.parseInt(pathParts[2]));
                }
                break;

            case "PUT":
                if (pathParts.length < 3) throw new IllegalArgumentException("ID missing for PUT request");
                controller.put(Integer.parseInt(pathParts[2]), decryptedBody);
                return "{\"message\": \"Resource updated successfully.\"}";

            case "DELETE":
                if (pathParts.length < 3) throw new IllegalArgumentException("ID missing for DELETE request");
                controller.delete(Integer.parseInt(pathParts[2]));
                return "{\"message\": \"Resource deleted successfully.\"}";

            default:
                throw new IllegalArgumentException("Method not allowed: " + method);
        }

        return null;
    }
}
