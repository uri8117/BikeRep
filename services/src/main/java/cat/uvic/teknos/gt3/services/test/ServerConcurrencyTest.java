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

    private static final String SERVER_URL = "http://localhost:8080/circuits";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                    {
                      "name": "Monza",
                      "location": "Italy",
                      "length": 5.793
                    }
                    """))
                .build();

        int numRequests = 5;
        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            futures.add(client.sendAsync(request, HttpResponse.BodyHandlers.ofString()));
        }

        for (int i = 0; i < futures.size(); i++) {
            HttpResponse<String> response = futures.get(i).get();
            System.out.println("Response " + (i + 1) + ": " + response.statusCode());
            System.out.println("Body " + (i + 1) + ": " + response.body());
        }
    }
}
