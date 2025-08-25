package com.arep;

import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HttpServerIntegrationTest {
    private static Process serverProcess;
    private static final int PORT = 35000;

    @BeforeAll
    public static void setUp() throws IOException, InterruptedException {
        serverProcess = new ProcessBuilder("java", "-cp", "target/classes", "com.arep.HttpServer")
                .redirectErrorStream(true)
                .start();
        
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(serverProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[SERVER] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        waitForServerStart(PORT, 15000);
    }

    private static void waitForServerStart(int port, int timeoutMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        boolean isPortOpen = false;
        
        while (!isPortOpen && (System.currentTimeMillis() - startTime) < timeoutMillis) {
            try (Socket socket = new Socket("localhost", port)) {
                isPortOpen = true;
                System.out.println("¡Servidor iniciado en el puerto " + port + "!");
            } catch (IOException e) {
                Thread.sleep(200);
            }
        }
        
        if (!isPortOpen) {
            throw new IllegalStateException("El servidor no inició en " + timeoutMillis + " ms");
        }
    }

    @AfterAll
    public static void tearDown() throws InterruptedException {
        if (serverProcess != null && serverProcess.isAlive()) {
            serverProcess.destroy();
            serverProcess.waitFor();
        }
    }

    @Test
    public void testServerResponse() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/components"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    
    @Test
    public void testPostComponent() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String jsonBody = "{\"name\":\"Interstellar\",\"type\":\"MOVIE\",\"description\":\"Ciencia ficción\",\"rating\":5}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/components"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }
}