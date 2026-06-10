package pe.edu.uni.centromedico.models;

public class Doctor extends Persona {
    private String especialidad;
    private String consultorio;
    private boolean activo;

    public Doctor() {
        super();
        this.setRol("DOCTOR");
    }

    public String getEspecialidad()     { return especialidad; }
    public String getConsultorio()      { return consultorio; }
    public boolean isActivo()           { return activo; }

    public void setEspecialidad(String e)   { this.especialidad = e; }
    public void setConsultorio(String c)    { this.consultorio = c; }
    public void setActivo(boolean a)        { this.activo = a; }
}
