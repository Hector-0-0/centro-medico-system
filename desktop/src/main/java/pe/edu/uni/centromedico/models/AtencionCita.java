package pe.edu.uni.centromedico.models;

import java.time.LocalDateTime;

public class AtencionCita {
    private int id;
    private int idCita;
    private String diagnostico;
    private String comentarios;
    private LocalDateTime fechaAtencion;

    public AtencionCita() {}

    public AtencionCita(int id, int idCita, String diagnostico,
        String comentarios, LocalDateTime fechaAtencion) {
        this.id = id;
        this.idCita = idCita;
        this.diagnostico = diagnostico;
        this.comentarios = comentarios;
        this.fechaAtencion = fechaAtencion;
    }

    public int getId()                      { return id; }
    public int getIdCita()                  { return idCita; }
    public String getDiagnostico()          { return diagnostico; }
    public String getComentarios()          { return comentarios; }
    public LocalDateTime getFechaAtencion() { return fechaAtencion; }

    public void setId(int id)                           { this.id = id; }
    public void setIdCita(int idCita)                   { this.idCita = idCita; }
    public void setDiagnostico(String d)                { this.diagnostico = d; }
    public void setComentarios(String c)                { this.comentarios = c; }
    public void setFechaAtencion(LocalDateTime f)       { this.fechaAtencion = f; }
}
