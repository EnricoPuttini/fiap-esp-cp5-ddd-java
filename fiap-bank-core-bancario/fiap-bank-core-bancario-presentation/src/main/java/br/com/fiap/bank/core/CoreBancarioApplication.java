package br.com.fiap.bank.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada. Fica no pacote raiz {@code br.com.fiap.bank.core}, pai de
 * domain, application, infrastructure e presentation, de modo que o scan padrao
 * do Spring Boot (componentes, entidades JPA e repositorios Spring Data) enxerga
 * todos os modulos sem precisar de anotacoes extras.
 */
@SpringBootApplication
public class CoreBancarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBancarioApplication.class, args);
    }
}
