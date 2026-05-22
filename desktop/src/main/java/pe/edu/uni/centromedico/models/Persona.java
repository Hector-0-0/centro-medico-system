package pe.edu.uni.centromedico.models;

public abstract class Persona {
    private String id;
    private String password;
    private String nombre;
    private String rol;

    public Persona() {}

    public Persona(String id, String password, String nombre, String rol) {
        this.id = id;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getId()           { return id; }
    public String getPassword()     { return password; }
    public String getNombre()       { return nombre; }
    public String getRol()          { return rol; }

    public void setId(String id)            { this.id = id; }
    public void setPassword(String pass)    { this.password = pass; }
    public void setNombre(String nombre)    { this.nombre = nombre; }
    public void setRol(String rol)          { this.rol = rol; }

    @Override
    public String toString() {
        return nombre + " [" + rol + "]";
    }
}