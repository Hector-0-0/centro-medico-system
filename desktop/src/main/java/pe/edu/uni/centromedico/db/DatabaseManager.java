package pe.edu.uni.centromedico.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static Connection connection;

    // URL sin base de datos — para poder crearla si no existe
    private static String getBaseUrl() {
        StringBuilder url = new StringBuilder("jdbc:sqlserver://")
            .append(ConfigManager.get("db.host"))
            .append(":").append(ConfigManager.get("db.port"));
        String inst = ConfigManager.get("db.instance");
        if (inst != null && !inst.isBlank()) {
            url.append(";instanceName=").append(inst.trim());
        }
        url.append(";encrypt=true;trustServerCertificate=true");
        return url.toString();
    }

    // URL con base de datos — para uso normal
    private static String getDbUrl() {
        return getBaseUrl() + ";databaseName=" + ConfigManager.get("db.name");
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                getDbUrl(),
                ConfigManager.get("db.user"),
                ConfigManager.get("db.password")
            );
        }
        return connection;
    }

    public static void inicializarDB() {
        String user = ConfigManager.get("db.user");
        String pass = ConfigManager.get("db.password");

        try (Connection conn = DriverManager.getConnection(getBaseUrl(), user, pass)) {

            String script;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        DatabaseManager.class.getResourceAsStream("/init_db.sql")))) {

                script = reader.lines().collect(Collectors.joining("\n"));
            }

            // SQL Server usa "GO" como separador de batches (case-insensitive, en su propia línea)
            for (String batch : script.split("(?im)^\\s*GO\\s*$")) {
                String s = batch.trim();
                if (s.isEmpty()) continue;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(s);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al inicializar la BD: " + e.getMessage());
        }
    }

    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
