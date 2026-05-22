package pe.edu.uni.centromedico.util;

import pe.edu.uni.centromedico.models.Persona;

public class SesionManager {

    private static Persona usuarioActual;

    private SesionManager() {}

    public static void iniciar(Persona p)  { usuarioActual = p; }
    public static void cerrar()            { usuarioActual = null; }

    public static Persona getUsuario()     { return usuarioActual; }
    public static String getId()           { return usuarioActual != null ? usuarioActual.getId()     : ""; }
    public static String getNombre()       { return usuarioActual != null ? usuarioActual.getNombre() : ""; }
    public static String getRol()          { return usuarioActual != null ? usuarioActual.getRol()    : ""; }
    public static boolean haySesion()      { return usuarioActual != null; }
}
