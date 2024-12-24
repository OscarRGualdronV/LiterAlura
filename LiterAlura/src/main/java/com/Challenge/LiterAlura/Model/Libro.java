package com.Challenge.LiterAlura.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Libro(
        @JsonProperty ("title")
        String titulo,
        @JsonProperty ("author")
        String autor,
        @JsonProperty ("languages")
        String idioma,
        @JsonProperty ("download_count")
        int descargas

) {}
