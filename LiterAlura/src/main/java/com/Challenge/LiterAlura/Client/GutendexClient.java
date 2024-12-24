package com.Challenge.LiterAlura.Client;

import com.Challenge.LiterAlura.Model.Libro;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class GutendexClient {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public GutendexClient() {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)  // Seguir redirecciones automáticamente
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<Libro> obtenerDatos(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            // Enviar la solicitud HTTP
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la respuesta fue exitosa (código HTTP 200)
            if (response.statusCode() == 200) {
                // Parsear la respuesta JSON
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode librosNode = rootNode.get("results"); // Obtener el array de libros desde el campo "results"

                // Convertir el JSON en una lista de objetos
                return mapearLibros(librosNode);
            } else {
                // Manejar el error si el código HTTP no es 200
                System.err.println("Error al obtener los datos: Código HTTP " + response.statusCode());
                throw new RuntimeException("Error al obtener los datos: Código HTTP " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            // Manejar errores de IO o interrupciones
            System.err.println("Error de IO: " + e.getMessage());
            throw new RuntimeException("Error al obtener los datos", e);
        }
    }

    private List<Libro> mapearLibros(JsonNode librosNode){
        // Verificamos si librosNode es un array
        if (librosNode.isArray()) {
            return StreamSupport.stream(librosNode.spliterator(), false)  // Convertimos el iterable en un stream
                    .map(libroNode -> new Libro(
                            libroNode.path("title").asText("Desconocido"),  // Título del libro
                            obtenerAutor(libroNode),  // Extraemos el autor
                            libroNode.path("languages").get(0).asText("Desconocido"),  // Idioma del libro
                            libroNode.path("download_count").asInt(0)  // Cantidad de descargas
                    ))
                    .collect(Collectors.toList());  // Convertimos el stream en una lista
        } else {
            // Si no es un array, retornamos una lista vacía o manejamos el error de forma adecuada
            return List.of();
        }
    }

    private String obtenerAutor(JsonNode libroNode) {
        JsonNode authorsNode = libroNode.path("authors");
        if (authorsNode.isArray() && !authorsNode.isEmpty()) {
            return authorsNode.get(0).path("name").asText("Autor Desconocido");
        }
        return "Autor Desconocido";
    }
}
