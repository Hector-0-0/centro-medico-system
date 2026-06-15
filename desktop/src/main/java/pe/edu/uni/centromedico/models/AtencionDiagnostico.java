package pe.edu.uni.centromedico.models;

public class AtencionDiagnostico {
    private int id;
    private int idAtencion;
    private int idCie;
    private String codigoCie;
    private String descripcionCie;
    private String observacion;

    public AtencionDiagnostico() {}

    public AtencionDiagnostico(int idCie, String codigoCie,
                               String descripcionCie, String observacion) {
        this.idCie = idCie;
        this.codigoCie = codigoCie;
        this.descripcionCie = descripcionCie;
        this.observacion = observacion;
    }

    public int getId()              { return id; }
    public int getIdAtencion()      { return idAtencion; }
    public int getIdCie()           { return idCie; }
    public String getCodigoCie()    { return codigoCie; }
    public String getDescripcionCie() { return descripcionCie; }
    public String getObservacion()  { return observacion; }

    public void setId(int id)                        { this.id = id; }
    public void setIdAtencion(int idAtencion)        { this.idAtencion = idAtencion; }
    public void setIdCie(int idCie)                  { this.idCie = idCie; }
    public void setCodigoCie(String codigoCie)       { this.codigoCie = codigoCie; }
    public void setDescripcionCie(String d)          { this.descripcionCie = d; }
    public void setObservacion(String observacion)   { this.observacion = observacion; }
}
