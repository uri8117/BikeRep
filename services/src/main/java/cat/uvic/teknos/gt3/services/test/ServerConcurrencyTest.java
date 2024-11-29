package cat.uvic.teknos.gt3.services.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ServerConcurrencyTest {

    private static final String SERVER_URL = "http://localhost:8090/users";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var client = HttpClient.newHttpClient();

        // Crear datos del usuario en formato JSON
        String userData = """
            {
              "name": "Concurrent User",
              "email": "concurrent.user@example.com"
            }
            """;

        // Construir la solicitud POST
        var request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(userData))
                .build();

        int numRequests = 10; // Número de solicitudes concurrentes
        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();

        // Enviar solicitudes concurrentes
        for (int i = 0; i < numRequests; i++) {
            futures.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }

        // Procesar y mostrar las respuestas
        for (int i = 0; i < futures.size(); i++) {
            HttpResponse<String> response = futures.get(i).get();
            System.out.println("Response " + (i + 1) + ": " + response.statusCode());
            System.out.println("Body " + (i + 1) + ": " + response.body());
        }
    }
}
