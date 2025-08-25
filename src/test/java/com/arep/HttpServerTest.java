package com.arep;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpServerTest {

    @Test
    public void testHandleApiRequestGet() throws IOException {
        HttpServer.components.clear();
        HttpServer.components.add(new Component("Test", "BOOK", "Descripción", 4));
        
        String request = "GET /api/components HTTP/1.1\r\n\r\n";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(request));
        OutputStream out = new BufferedOutputStream(outputStream);

        HttpServer.handleApiRequest("GET", "/api/components", in, out);

        String response = outputStream.toString(StandardCharsets.UTF_8.name());
        System.out.println(response);

        assertTrue(response.contains("200 OK"), "La respuesta debe contener '200 OK'");
        assertTrue(response.contains("Test"), "La respuesta debe contener el componente de prueba");
        assertTrue(response.contains("BOOK"), "La respuesta debe contener el tipo");
        assertTrue(response.contains("Descripción"), "La respuesta debe contener la descripción");
        assertTrue(response.contains("\"rating\":4"), "La respuesta debe contener la calificación");
    }

    @Test
    public void testHandleApiRequestPost() throws IOException {
        HttpServer.components.clear();
        String jsonBody = "{\"name\":\"Breaking Bad\",\"type\":\"SHOW\",\"description\":\"Serie de TV\",\"rating\":5}";
        int contentLength = jsonBody.length();
        
        String request = "POST /api/components HTTP/1.1\r\n" +
                         "Content-Length: " + contentLength + "\r\n\r\n" + 
                         jsonBody;
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(request));
        OutputStream out = new BufferedOutputStream(outputStream);

        HttpServer.handleApiRequest("POST", "/api/components", in, out);

        String response = outputStream.toString(StandardCharsets.UTF_8.name());
        System.out.println(response);
        
        assertTrue(response.contains("201 Created"), "Respuesta: " + response);
        assertEquals(1, HttpServer.components.size(), "Debe haber un componente en la lista");
        
        Component added = HttpServer.components.get(0);
        assertEquals("Breaking Bad", added.getName());
        assertEquals("SHOW", added.getType());
        assertEquals("Serie de TV", added.getDescription());
        assertEquals(5, added.getRating());
    }
    
    @Test
    public void testInvalidJsonPost() throws IOException {
        HttpServer.components.clear();
        String request = "POST /api/components HTTP/1.1\r\nContent-Length: 20\r\n\r\n" + 
                         "Invalid JSON format";
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(request));
        OutputStream out = new BufferedOutputStream(outputStream);

        HttpServer.handleApiRequest("POST", "/api/components", in, out);

        String response = outputStream.toString(StandardCharsets.UTF_8.name());
        System.out.println(response);
        
        assertTrue(response.contains("400 Bad Request"));
        assertTrue(response.contains("Invalid JSON format"));
    }
    
    @Test
    public void testMissingFieldsPost() throws IOException {
        HttpServer.components.clear();
        String request = "POST /api/components HTTP/1.1\r\nContent-Length: 48\r\n\r\n" + 
                         "{\"name\":\"Solo nombre\",\"type\":\"MOVIE\"}";
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new StringReader(request));
        OutputStream out = new BufferedOutputStream(outputStream);

        HttpServer.handleApiRequest("POST", "/api/components", in, out);

        String response = outputStream.toString(StandardCharsets.UTF_8.name());
        System.out.println(response);
        
        assertTrue(response.contains("400 Bad Request"));
        assertTrue(response.contains("Missing fields"));
    }
}