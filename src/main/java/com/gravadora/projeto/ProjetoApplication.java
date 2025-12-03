package com.gravadora.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoApplication.class, args);
		System.out.println("Sistema Iniciado!!");
		System.out.println("Acesse: http://localhost:8080");
	}
}
