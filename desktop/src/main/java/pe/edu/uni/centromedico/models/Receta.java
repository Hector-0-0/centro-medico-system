package pe.edu.uni.centromedico.models;

import java.util.List;
import java.util.ArrayList;

public class Receta {
    private int    id;
    private int    idAtencion;
    private String estado;           // PENDIENTE, ENTREGADA
    private List<RecetaDetalle> detalles;

    // Campos de display para mostrar en tabla sin JOINs adicionales
    private String nombrePaciente;
    private String diagnostico;
    private int    idCita;

    public Receta() {
        this.detalles = new ArrayList<>();
    }

    public Receta(int id, int idAtencion, String estado) {
        this.id        = id;
        this.idAtencion = idAtencion;
        this.estado    = estado;
        this.detalles  = new ArrayList<>();
    }

    public int    getId()                             { return id; }
    public int    getIdAtencion()                     { return idAtencion; }
    public String getEstado()                         { return estado; }
    public List<RecetaDetalle> getDetalles()          { return detalles; }
    public String getNombrePaciente()                 { return nombrePaciente; }
    public String getDiagnostico()                    { return diagnostico; }
    public int    getIdCita()                         { return idCita; }

    public void setId(int id)                         { this.id = id; }
    public void setIdAtencion(int ida)                { this.idAtencion = ida; }
    public void setEstado(String estado)              { this.estado = estado; }
    public void setDetalles(List<RecetaDetalle> d)    { this.detalles = d; }
    public void setNombrePaciente(String nombre)      { this.nombrePaciente = nombre; }
    public void setDiagnostico(String diagnostico)    { this.diagnostico = diagnostico; }
    public void setIdCita(int idCita)                 { this.idCita = idCita; }

    public void agregarDetalle(RecetaDetalle detalle) { this.detalles.add(detalle); }
}
