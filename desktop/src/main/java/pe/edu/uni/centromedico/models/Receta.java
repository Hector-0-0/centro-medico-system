package pe.edu.uni.centromedico.models;

import java.util.List;
import java.util.ArrayList;

public class Receta {
    private int id;
    private int idAtencion;
    private String estado; // PENDIENTE, ENTREGADA
    private List<RecetaDetalle> detalles;

    public Receta() {
        this.detalles = new ArrayList<>();
    }

    public Receta(int id, int idAtencion, String estado) {
        this.id = id;
        this.idAtencion = idAtencion;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }

    public int getId()                          { return id; }
    public int getIdAtencion()                  { return idAtencion; }
    public String getEstado()                   { return estado; }
    public List<RecetaDetalle> getDetalles()    { return detalles; }

    public void setId(int id)                   { this.id = id; }
    public void setIdAtencion(int ida)          { this.idAtencion = ida; }
    public void setEstado(String estado)        { this.estado = estado; }
    public void setDetalles(List<RecetaDetalle> d) { this.detalles = d; }

    public void agregarDetalle(RecetaDetalle detalle) {
        this.detalles.add(detalle);
    }
}
