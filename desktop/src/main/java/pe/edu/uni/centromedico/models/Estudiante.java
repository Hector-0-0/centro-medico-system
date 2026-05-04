package pe.edu.uni.centromedico.models;

public class Estudiante extends Persona {
    public int age; 
    public String especialidad;  
    public Estudiante() {
        this.rol = "PACIENTE";
    }
}
