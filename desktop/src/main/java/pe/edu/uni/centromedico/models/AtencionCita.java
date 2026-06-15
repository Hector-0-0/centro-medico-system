package pe.edu.uni.centromedico.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class AtencionCita {
    private int id;
    private int idCita;
    private String diagnostico;
    private String comentarios;
    private LocalDateTime fechaAtencion;
    private List<AtencionDiagnostico> diagnosticos;

    public AtencionCita() {
        this.diagnosticos = new ArrayList<>();
    }

    public AtencionCita(int id, int idCita, String diagnostico,
        String comentarios, LocalDateTime fechaAtencion) {
        this.id = id;
        this.idCita = idCita;
        this.diagnostico = diagnostico;
        this.comentarios = comentarios;
        this.fechaAtencion = fechaAtencion;
        this.diagnosticos = new ArrayList<>();
    }

    public int getId()                      { return id; }
    public int getIdCita()                  { return idCita; }
    public String getDiagnostico()          { return diagnostico; }
    public String getComentarios()          { return comentarios; }
    public LocalDateTime getFechaAtencion() { return fechaAtencion; }
    public List<AtencionDiagnostico> getDiagnosticos() { return diagnosticos; }

    public void setId(int id)                           { this.id = id; }
    public void setIdCita(int idCita)                   { this.idCita = idCita; }
    public void setDiagnostico(String d)                { this.diagnostico = d; }
    public void setComentarios(String c)                { this.comentarios = c; }
    public void setFechaAtencion(LocalDateTime f)       { this.fechaAtencion = f; }
    public void setDiagnosticos(List<AtencionDiagnostico> diags) { this.diagnosticos = diags; }
}
