package edu.universidad.centromedico.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Crea y siembra la base de datos SQL Server al arrancar, ejecutando el MISMO
 * script `init_db.sql` que usa la app de escritorio. Es idempotente (el script
 * usa IF NOT EXISTS), por lo que web y desktop comparten esquema y semilla.
 *
 * Se conecta SIN databaseName (a `master`) para poder crear la BD si no existe,
 * tal como hace DatabaseManager.inicializarDB() en el desktop.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void run(String... args) {
        // URL base sin databaseName: permite crear la BD si todavía no existe.
        String baseUrl = url.replaceFirst(";databaseName=[^;]+", "");

        try (Connection conn = DriverManager.getConnection(baseUrl, username, password)) {

            String script;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new ClassPathResource("init_db.sql").getInputStream(), StandardCharsets.UTF_8))) {
                script = reader.lines().collect(Collectors.joining("\n"));
            }

            // SQL Server separa batches con "GO" (en su propia línea).
            for (String batch : script.split("(?im)^\\s*GO\\s*$")) {
                String s = batch.trim();
                if (s.isEmpty()) continue;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(s);
                }
            }
            System.out.println("✅ Base de datos SQL Server inicializada (init_db.sql)");

        } catch (Exception e) {
            System.err.println("⚠️  No se pudo inicializar la BD: " + e.getMessage());
        }
    }
}
