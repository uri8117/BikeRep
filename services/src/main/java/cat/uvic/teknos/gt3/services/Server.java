package cat.uvic.teknos.gt3.services;

import cat.uvic.teknos.gt3.services.exception.ServerException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpOptions;
import rawhttp.core.RawHttpResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final String path = "services/src/main/resources/server.properties";
    public final int PORT = 8070;
    private final RequestRouter requestRouter;
    private volatile boolean SHUTDOWN_SERVER = false;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10); // Thread pool per gestionar les sol·licituds
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(); // Scheduler per comprovar l'estat de shutdown

    public Server(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
        startShutdownChecker(); // Començar a comprovar si s'ha de parar el servidor
    }

    // Comença el servidor
    public void start() {
        try (var serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (!SHUTDOWN_SERVER) { // Executa fins que s'eviti el shutdown
                try {
                    var clientSocket = serverSocket.accept(); // Espera connexions entrants
                    threadPool.submit(() -> handleClientRequest(clientSocket)); // Enviar la connexió a un thread del pool
                } catch (IOException e) {
                    if (!SHUTDOWN_SERVER) {
                        throw new ServerException(e); // Llençar excepció si no és un tancament normal
                    }
                }
            }
        } catch (IOException e) {
            throw new ServerException(e); // Llençar excepció si hi ha algun error amb el socket
        } finally {
            shutdownThreadPool(); // Aturar el thread pool quan el servidor es tanqui
            shutdownScheduler(); // Aturar el scheduler quan el servidor es tanqui
        }
    }

    // Gestió de les peticions dels clients
    private void handleClientRequest(Socket clientSocket) {
        try (clientSocket) {
            System.out.println("Handling request at: " + System.currentTimeMillis());
            var rawHttp = new RawHttp(RawHttpOptions.newBuilder().doNotInsertHostHeaderIfMissing().build());
            var request = rawHttp.parseRequest(clientSocket.getInputStream());

            RawHttpResponse<?> response = requestRouter.execRequest(request); // Enviar la petició al router i obtenir la resposta

            response.writeTo(clientSocket.getOutputStream()); // Escrivim la resposta al socket del client
        } catch (Exception e) {
            System.err.println("Error handling client request: " + e.getMessage());
        }
    }

    // Comprova si s'ha de tancar el servidor cada 5 segons
    private void startShutdownChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            try (var input = new FileInputStream(path)) {
                Properties properties = new Properties();
                properties.load(input);
                String shutdownValue = properties.getProperty("shutdown", "false");

                if (Boolean.parseBoolean(shutdownValue)) { // Si la propietat "shutdown" és "true", tancar el servidor
                    System.out.println("Shutdown signal received. Stopping server...");
                    stop();
                }
            } catch (IOException e) {
                System.err.println("Error reading server.properties: " + e.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS); // Comprovar cada 5 segons
    }

    // Aturar el servidor de forma segura
    public void stop() {
        SHUTDOWN_SERVER = true;
        shutdownThreadPool();
        shutdownScheduler();
    }

    // Aturar el thread pool de manera segura
    private void shutdownThreadPool() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // Si no es tanca a temps, forçar la parada
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow(); // Si el thread pool s'interromp, aturar-lo de manera immediata
            Thread.currentThread().interrupt();
        }
    }

    // Aturar el scheduler de manera segura
    private void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow(); // Si no es tanca a temps, forçar la parada
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow(); // Si el scheduler s'interromp, aturar-lo de manera immediata
            Thread.currentThread().interrupt();
        }
    }
}
