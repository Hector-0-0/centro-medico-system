package pe.edu.uni.centromedico.models;

import java.time.LocalDate;
import java.time.Period;

public class Estudiante extends Persona {
    private String carrera;
    private String email;
    private LocalDate fechaNacimiento;

    public Estudiante() {
        super();
        this.setRol("ESTUDIANTE");
    }

    public int getEdad() {
        return fechaNacimiento == null ? 0
            : Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    public String getCarrera()          { return carrera; }
    public String getEmail()            { return email; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    public void setCarrera(String carrera)              { this.carrera = carrera; }
    public void setEmail(String email)                  { this.email = email; }
    public void setFechaNacimiento(LocalDate fecha)     { this.fechaNacimiento = fecha; }
}
