package pe.edu.uni.centromedico.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Lee SGH.config (ubicado en la raíz del proyecto) y expone las rutas
 * configurables como variables públicas, evitando rutas "quemadas" en
 * el código fuente.
 *
 * Uso:
 *     ConfiguracionParametros OBJ = new ConfiguracionParametros();
 *     ImageIcon logo = new ImageIcon(OBJ.PathImages + "logo.png");
 *
 * Si SGH.config no existe o la clave no está, hace fallback al classpath
 * para que la app siga funcionando aunque el archivo no esté configurado.
 */
public class ConfiguracionParametros {

    public String PathImages;

    public ConfiguracionParametros() {
        Properties props = new Properties();
        File config = new File("SGH.config");

        if (config.exists()) {
            try (FileReader reader = new FileReader(config)) {
                props.load(reader);
                String ruta = props.getProperty("IMAGES");
                if (ruta != null && !ruta.isBlank()) {
                    PathImages = ruta.trim();
                    if (!PathImages.endsWith(File.separator)
                     && !PathImages.endsWith("/")) {
                        PathImages += File.separator;
                    }
                    return;
                }
            } catch (IOException e) {
                System.err.println("Error al leer SGH.config: " + e.getMessage());
            }
        }

        // Fallback: si no hay config, intenta resolver el classpath
        PathImages = "";
    }

    /**
     * Carga una imagen respetando la configuración:
     *   1. Si SGH.config define IMAGES → la carga desde ahí
     *   2. Si no existe en disco → fallback al classpath (recursos embebidos)
     *   3. Si tampoco existe → devuelve null (caller debe manejarlo)
     */
    public javax.swing.ImageIcon cargarIcono(String nombreArchivo) {
        if (!PathImages.isEmpty()) {
            File f = new File(PathImages + nombreArchivo);
            if (f.exists()) return new javax.swing.ImageIcon(f.getAbsolutePath());
        }
        // Fallback al classpath
        java.net.URL url = getClass().getResource("/Images/" + nombreArchivo);
        if (url == null) url = getClass().getResource("/" + nombreArchivo);
        return url != null ? new javax.swing.ImageIcon(url) : null;
    }
}
