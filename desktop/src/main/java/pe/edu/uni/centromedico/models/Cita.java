package pe.edu.uni.centromedico.models;

public class Cita {
    private int id;
    private String idEstudiante;
    private String idDoctor;
    private int idSlot;
    private String motivo;
    private String estado; // PENDIENTE, ATENDIDA, CANCELADA, NO_ASISTIO
    private String nombreEstudiante;
    private String nombreDoctor;
    private String especialidad;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private String fechaCreacion;

    public Cita() {}

    public Cita(int id, String idEstudiante, String idDoctor,
        int idSlot, String motivo, String estado) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idDoctor = idDoctor;
        this.idSlot = idSlot;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getId()              { return id; }
    public String getIdEstudiante() { return idEstudiante; }
    public String getIdDoctor()     { return idDoctor; }
    public int getIdSlot()       { return idSlot; }
    public String getMotivo()       { return motivo; }
    public String getEstado()       { return estado; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getNombreDoctor()     { return nombreDoctor; }
    public String getEspecialidad()     { return especialidad; }
    public String getDiaSemana()        { return diaSemana; }
    public String getHoraInicio()       { return horaInicio; }
    public String getHoraFin()          { return horaFin; }

    public void setId(int id)                       { this.id = id; }
    public void setIdEstudiante(String ide)         { this.idEstudiante = ide; }
    public void setIdDoctor(String idd)             { this.idDoctor = idd; }
    public void setIdSlot(int idh)               { this.idSlot = idh; }
    public void setMotivo(String motivo)            { this.motivo = motivo; }
    public void setEstado(String estado)            { this.estado = estado; }
    public void setNombreEstudiante(String n)  { this.nombreEstudiante = n; }
    public void setNombreDoctor(String n)      { this.nombreDoctor = n; }
    public void setEspecialidad(String e)      { this.especialidad = e; }
    public void setDiaSemana(String d)         { this.diaSemana = d; }
    public void setHoraInicio(String h)        { this.horaInicio = h; }
    public void setHoraFin(String h)           { this.horaFin = h; }
    public String getFechaCreacion()             { return fechaCreacion; }
    public void setFechaCreacion(String fc)      { this.fechaCreacion = fc; }
}