package pe.edu.uni.centromedico.models;

public class Cita {
    private int id;
    private String idEstudiante;
    private String idDoctor;
    private int idHorario;
    private String motivo;
    private String estado; // PENDIENTE, ATENDIDA, CANCELADA
    private String nombreEstudiante;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;

    public Cita() {}

    public Cita(int id, String idEstudiante, String idDoctor,
        int idHorario, String motivo, String estado) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idDoctor = idDoctor;
        this.idHorario = idHorario;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getId()              { return id; }
    public String getIdEstudiante() { return idEstudiante; }
    public String getIdDoctor()     { return idDoctor; }
    public int getIdHorario()       { return idHorario; }
    public String getMotivo()       { return motivo; }
    public String getEstado()       { return estado; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getDiaSemana()        { return diaSemana; }
    public String getHoraInicio()       { return horaInicio; }
    public String getHoraFin()          { return horaFin; }

    public void setId(int id)                       { this.id = id; }
    public void setIdEstudiante(String ide)         { this.idEstudiante = ide; }
    public void setIdDoctor(String idd)             { this.idDoctor = idd; }
    public void setIdHorario(int idh)               { this.idHorario = idh; }
    public void setMotivo(String motivo)            { this.motivo = motivo; }
    public void setEstado(String estado)            { this.estado = estado; }
    public void setNombreEstudiante(String n) { this.nombreEstudiante = n; }
    public void setDiaSemana(String d)        { this.diaSemana = d; }
    public void setHoraInicio(String h)       { this.horaInicio = h; }
    public void setHoraFin(String h)          { this.horaFin = h; }
}