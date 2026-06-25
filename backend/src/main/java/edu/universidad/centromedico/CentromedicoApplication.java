package edu.universidad.centromedico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Punto de entrada principal del sistema de gestión del Centro Médico UNI.
 */
@SpringBootApplication
@EnableAsync
public class CentromedicoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentromedicoApplication.class, args);
    }
}
