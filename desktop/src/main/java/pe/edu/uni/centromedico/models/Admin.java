package pe.edu.uni.centromedico.models;

public class Admin extends Persona {

    public Admin() {
        super();
        this.setRol("ADMIN");
    }

    public Admin(String id, String password, String nombre) {
        super(id, password, nombre, "ADMIN");
    }
}