package pe.edu.uni.centromedico.db;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigManager.class
                .getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new RuntimeException(
                    "No se encontró config.properties en resources/");
            }
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar config.properties: "
                + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}