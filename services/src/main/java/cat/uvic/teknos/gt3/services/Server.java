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
    public final int PORT = 8090;
    private final RequestRouter requestRouter;
    private volatile boolean SHUTDOWN_SERVER = false;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Server(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
        startShutdownChecker();
    }

    public void start() {
        try (var serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (!SHUTDOWN_SERVER) {
                try {
                    var clientSocket = serverSocket.accept();

                    threadPool.submit(() -> handleClientRequest(clientSocket));
                } catch (IOException e) {
                    if (!SHUTDOWN_SERVER) {
                        throw new ServerException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new ServerException(e);
        } finally {
            shutdownThreadPool();
            shutdownScheduler();
        }
    }

    private void handleClientRequest(Socket clientSocket) {
        try (clientSocket) {
            System.out.println("Handling request at: " + System.currentTimeMillis());
            var rawHttp = new RawHttp(RawHttpOptions.newBuilder().doNotInsertHostHeaderIfMissing().build());
            var request = rawHttp.parseRequest(clientSocket.getInputStream());

            RawHttpResponse<?> response = requestRouter.execRequest(request);

            response.writeTo(clientSocket.getOutputStream());
        } catch (Exception e) {
            System.err.println("Error handling client request: " + e.getMessage());
        }
    }

    private void startShutdownChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            try (var input = new FileInputStream(path)) {
                Properties properties = new Properties();
                properties.load(input);
                String shutdownValue = properties.getProperty("shutdown", "false");

                if (Boolean.parseBoolean(shutdownValue)) {
                    System.out.println("Shutdown signal received. Stopping server...");
                    stop();
                }
            } catch (IOException e) {
                System.err.println("Error reading server.properties: " + e.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void stop() {
        SHUTDOWN_SERVER = true;
        shutdownThreadPool();
        shutdownScheduler();
    }

    private void shutdownThreadPool() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
