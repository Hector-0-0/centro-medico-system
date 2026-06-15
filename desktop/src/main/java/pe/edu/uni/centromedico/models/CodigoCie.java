package pe.edu.uni.centromedico.models;

public class CodigoCie {
    private int id;
    private String codigo;
    private String descripcion;

    public CodigoCie() {}

    public CodigoCie(int id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getId()                { return id; }
    public String getCodigo()         { return codigo; }
    public String getDescripcion()    { return descripcion; }

    public void setId(int id)                      { this.id = id; }
    public void setCodigo(String codigo)           { this.codigo = codigo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
}
