package pe.edu.uni.centromedico.db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static Connection connection;

    // URL sin base de datos — para poder crearla si no existe
    private static String getBaseUrl() {
        return "jdbc:mysql://"
            + ConfigManager.get("db.host") + ":"
            + ConfigManager.get("db.port")
            + "?useSSL=false&serverTimezone=America/Lima"
            + "&allowPublicKeyRetrieval=true";
    }

    // URL con base de datos — para uso normal
    private static String getDbUrl() {
        return "jdbc:mysql://"
            + ConfigManager.get("db.host") + ":"
            + ConfigManager.get("db.port") + "/"
            + ConfigManager.get("db.name")
            + "?useSSL=false&serverTimezone=America/Lima"
            + "&allowPublicKeyRetrieval=true";
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
        System.out.println("Iniciando configuración de base de datos...");

        String user = ConfigManager.get("db.user");
        String pass = ConfigManager.get("db.password");

        try (Connection conn = DriverManager.getConnection(
                getBaseUrl(), user, pass)) {

            // Leer el script SQL desde resources
            String script;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        DatabaseManager.class
                            .getResourceAsStream("/init_db.sql")))) {

                script = reader.lines()
                    .collect(Collectors.joining("\n"));
            }

            // Ejecutar sentencia por sentencia
            for (String sentencia : script.split(";")) {
                String s = sentencia.trim();
                if (!s.isEmpty()) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(s);
                    }
                }
            }

            System.out.println("Base de datos lista.");

        } catch (Exception e) {
            System.err.println("Error al inicializar la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}