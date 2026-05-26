package pe.edu.uni.centromedico.models;

public class Slot {
    private int id;
    private int idDisponibilidad; // ← nuevo
    private String idDoctor;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private boolean disponible;
    private boolean eliminado; // ← nuevo
    // campos extra para tabla
    private String nombreDoctor;
    private String especialidad;
    private String consultorio;

    public Slot() {
    }

    public Slot(int id, int idDisponibilidad, String idDoctor,
            String diaSemana, String horaInicio, String horaFin,
            boolean disponible) {
        this.id = id;
        this.idDisponibilidad = idDisponibilidad;
        this.idDoctor = idDoctor;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public int getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public String getIdDoctor() {
        return idDoctor;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdDisponibilidad(int id) {
        this.idDisponibilidad = id;
    }

    public void setIdDoctor(String v) {
        this.idDoctor = v;
    }

    public void setDiaSemana(String v) {
        this.diaSemana = v;
    }

    public void setHoraInicio(String v) {
        this.horaInicio = v;
    }

    public void setHoraFin(String v) {
        this.horaFin = v;
    }

    public void setDisponible(boolean v) {
        this.disponible = v;
    }

    public void setEliminado(boolean v) {
        this.eliminado = v;
    }

    public void setNombreDoctor(String v) {
        this.nombreDoctor = v;
    }

    public void setEspecialidad(String v) {
        this.especialidad = v;
    }

    public void setConsultorio(String v) {
        this.consultorio = v;
    }
}