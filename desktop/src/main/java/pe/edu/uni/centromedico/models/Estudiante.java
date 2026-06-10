package pe.edu.uni.centromedico.models;

public class Estudiante extends Persona {
    private int edad;
    private String carrera;
    private String email;

    public Estudiante() {
        super();
        this.setRol("ESTUDIANTE");
    }

    public int getEdad()        { return edad; }
    public String getCarrera()  { return carrera; }
    public String getEmail()    { return email; }

    public void setEdad(int edad)           { this.edad = edad; }
    public void setCarrera(String carrera)  { this.carrera = carrera; }
    public void setEmail(String email)      { this.email = email; }
}
