package pe.edu.uni.centromedico.models;

public class Farmacia extends Persona {

    public Farmacia() {
        super();
        this.setRol("FARMACIA");
    }

    public Farmacia(String id, String password, String nombre) {
        super(id, password, nombre, "FARMACIA");
    }
}