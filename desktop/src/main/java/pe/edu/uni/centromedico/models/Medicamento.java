package pe.edu.uni.centromedico.models;

public class Medicamento {
    private String id;
    private String nombre;
    private int stock;
    private String tipo;
    private String dosis;

    public Medicamento() {}

    public Medicamento(String id, String nombre, int stock, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.tipo = tipo;
    }

    public String getId()       { return id; }
    public String getNombre()   { return nombre; }
    public int getStock()       { return stock; }
    public String getTipo()     { return tipo; }
    public String getDosis()    { return dosis; }

    public void setId(String id)        { this.id = id; }
    public void setNombre(String n)     { this.nombre = n; }
    public void setStock(int stock)     { this.stock = stock; }
    public void setTipo(String tipo)    { this.tipo = tipo; }
    public void setDosis(String dosis)  { this.dosis = dosis; }
}
