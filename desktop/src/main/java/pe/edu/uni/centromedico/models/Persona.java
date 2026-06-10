package pe.edu.uni.centromedico.models;

public abstract class Persona {
    private String id;
    private String nombre;
    private String rol;

    public Persona() {}

    public String getId()       { return id; }
    public String getNombre()   { return nombre; }
    public String getRol()      { return rol; }

    public void setId(String id)         { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRol(String rol)       { this.rol = rol; }

    @Override
    public String toString() {
        return nombre + " [" + rol + "]";
    }
}
