package pe.edu.uni.centromedico.models;

public class Disponibilidad {
    private int id;
    private String idDoctor;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private boolean eliminado;

    public Disponibilidad() {}

    public Disponibilidad(String idDoctor, String diaSemana,
                        String horaInicio, String horaFin) {
        this.idDoctor = idDoctor;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getId()              { return id; }
    public String getIdDoctor()     { return idDoctor; }
    public String getDiaSemana()    { return diaSemana; }
    public String getHoraInicio()   { return horaInicio; }
    public String getHoraFin()      { return horaFin; }
    public boolean isEliminado()    { return eliminado; }

    public void setId(int id)               { this.id = id; }
    public void setIdDoctor(String v)       { this.idDoctor = v; }
    public void setDiaSemana(String v)      { this.diaSemana = v; }
    public void setHoraInicio(String v)     { this.horaInicio = v; }
    public void setHoraFin(String v)        { this.horaFin = v; }
    public void setEliminado(boolean v)     { this.eliminado = v; }
}