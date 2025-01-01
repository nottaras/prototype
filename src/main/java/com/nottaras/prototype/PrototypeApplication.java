package com.nottaras.prototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@ConfigurationPropertiesScan("com.nottaras.prototype.config.prop")
public class PrototypeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrototypeApplication.class, args);
    }

}
