package br.com.fiap.bank.core.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Ponto de entrada da aplicação. Como domain/application/infrastructure
 * vivem em pacotes-irmãos de presentation (todos sob br.com.fiap.bank.core),
 * é preciso declarar explicitamente onde o Spring deve procurar: componentes
 * (@ComponentScan), entidades JPA (@EntityScan) e repositórios Spring Data
 * (@EnableJpaRepositories) - o scan automático padrão só olharia para
 * baixo do pacote desta classe.
 */
@SpringBootApplication
@ComponentScan(basePackages = "br.com.fiap.bank.core")
@EntityScan(basePackages = "br.com.fiap.bank.core.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "br.com.fiap.bank.core.infrastructure.persistence.jpa")
public class CoreBancarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBancarioApplication.class, args);
    }
}
