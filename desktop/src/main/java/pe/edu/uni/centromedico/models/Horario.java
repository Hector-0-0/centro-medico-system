package pe.edu.uni.centromedico.models;

public class Horario {
    private int id;
    private String idDoctor;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;
    private String nombreDoctor;
    private String especialidad;
    private String consultorio;
    public Horario() {}

    public Horario(int id, String idDoctor, String diaSemana,
    String horaInicio, String horaFin, boolean disponible) {
        this.id = id;
        this.idDoctor = idDoctor;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = disponible;
}

    public int getId()              { return id; }
    public String getIdDoctor()     { return idDoctor; }
    public String getDiaSemana()    { return diaSemana; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin()    { return horaFin; }
    public boolean isDisponible()   { return disponible; }
    public String getNombreDoctor() { return nombreDoctor; }
    public String getEspecialidad() { return especialidad; }
    public String getConsultorio()  { return consultorio; }

    public void setId(int id)                   { this.id = id; }
    public void setIdDoctor(String idDoctor)    { this.idDoctor = idDoctor; }
    public void setDiaSemana(String dia)        { this.diaSemana = dia; }
    public void setHoraInicio(String h) { this.horaInicio = h; }
    public void setHoraFin(String h)    { this.horaFin = h; }
    public void setDisponible(boolean d)        { this.disponible = d; }
    public void setNombreDoctor(String n) { this.nombreDoctor = n; }
    public void setEspecialidad(String e) { this.especialidad = e; }
    public void setConsultorio(String c)  { this.consultorio = c; }
}
