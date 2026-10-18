package br.com.fiap.bank.plataforma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Ponto de entrada. Fica no pacote raiz {@code br.com.fiap.bank.plataforma}, pai de
 * domain, application, infrastructure e presentation, então o scan padrão do Spring
 * Boot (componentes, entidades JPA, repositórios Spring Data e clients Feign)
 * enxerga todos os módulos sem anotações extras de pacote.
 */
@SpringBootApplication
@EnableFeignClients
public class PlataformaCreditoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlataformaCreditoApplication.class, args);
    }
}
