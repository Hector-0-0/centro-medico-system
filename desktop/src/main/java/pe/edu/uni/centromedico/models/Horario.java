package pe.edu.uni.centromedico.models;

public class Horario {
    private int id;
    private String idDoctor;
    private String diaSemana;
    private String hora;
    private boolean disponible;

    public Horario() {}

    public Horario(int id, String idDoctor, String diaSemana,
        String hora, boolean disponible) {
        this.id = id;
        this.idDoctor = idDoctor;
        this.diaSemana = diaSemana;
        this.hora = hora;
        this.disponible = disponible;
    }

    public int getId()              { return id; }
    public String getIdDoctor()     { return idDoctor; }
    public String getDiaSemana()    { return diaSemana; }
    public String getHora()         { return hora; }
    public boolean isDisponible()   { return disponible; }

    public void setId(int id)                   { this.id = id; }
    public void setIdDoctor(String idDoctor)    { this.idDoctor = idDoctor; }
    public void setDiaSemana(String dia)        { this.diaSemana = dia; }
    public void setHora(String hora)            { this.hora = hora; }
    public void setDisponible(boolean d)        { this.disponible = d; }
}
