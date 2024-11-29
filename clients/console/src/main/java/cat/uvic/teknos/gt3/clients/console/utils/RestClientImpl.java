package cat.uvic.teknos.gt3.clients.console.utils;

import cat.uvic.teknos.gt3.clients.console.exceptions.ConsoleClientException;
import cat.uvic.teknos.gt3.clients.console.exceptions.RequestException;
import rawhttp.core.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class RestClientImpl {
    private final int port;
    private final String host;

    public RestClientImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T get(String path, Class<T> returnType) throws RequestException {
        return execRequest("GET", path, null, returnType);
    }

    public <T> T[] getAll(String path, Class<T[]> returnType) throws RequestException {
        return execRequest("GET", path, null, returnType);
    }

    public void post(String path, String body) throws RequestException {
        execRequest("POST", path, body, Void.class);
    }

    public void put(String path, String body) throws RequestException {
        execRequest("PUT", path, body, Void.class);
    }

    public void delete(String path, String body) throws RequestException {
        execRequest("DELETE", path, body, Void.class);
    }

    protected <T> T execRequest(String method, String path, String body, Class<T> returnType) throws RequestException {
        var rawHttp = new RawHttp();
        try (var socket = new Socket(host, port)) {

            if (body == null) {
                body = "";
            }

            // Configuramos la solicitud HTTP
            var request = rawHttp.parseRequest(
                    method + " " + String.format("http://%s:%d/%s", host, port, path) + " HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "User-Agent: RawHTTP\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Accept: application/json\r\n" +
                            "\r\n" +
                            body
            );

            request.writeTo(socket.getOutputStream());

            T returnValue = null;
            var response = rawHttp.parseResponse(socket.getInputStream()).eagerly();

            // Verificar si la respuesta es un código HTTP de éxito (200 OK o 201 para POST)
            int responseCode = response.getStatusCode();
            if (responseCode != 200 && responseCode != 201) {
                throw new RequestException("Request failed with HTTP code " + responseCode);
            }

            if (!returnType.isAssignableFrom(Void.class)) {
                returnValue = Mappers.get().readValue(response.getBody().get().toString(), returnType);
            }

            return returnValue;
        } catch (IOException e) {
            throw new RequestException("Error executing request: " + e.getMessage(), e);
        }
    }
}
