package com.Challenge.LiterAlura;

import com.Challenge.LiterAlura.Client.GutendexClient;
import com.Challenge.LiterAlura.Model.Libro;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	public static void main(String[] args) { SpringApplication.run(LiterAluraApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		String url = "https://gutendex.com/books/?languages=es";
		GutendexClient consumoApi = new GutendexClient();

		List<Libro> libros = consumoApi.obtenerDatos(url);

		// Imprimir los libros obtenidos
		libros.forEach(libro -> System.out.println("Título: " + libro.titulo() +
				", Autor: " + libro.autor() +
				", Idioma: " + libro.idioma() +
				", Descargas: " + libro.descargas()));

	}
}