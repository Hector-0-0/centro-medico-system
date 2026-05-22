package pe.edu.uni.centromedico.models;

public class RecetaDetalle {
    private int id;
    private int idReceta;
    private String idMedicamento;
    private String nombreMedicamento; // para mostrar en UI sin join extra
    private String dosis;
    private String duracion;

    public RecetaDetalle() {}

    public RecetaDetalle(int id, int idReceta, String idMedicamento,
        String nombreMedicamento, String dosis, String duracion) {
        this.id = id;
        this.idReceta = idReceta;
        this.idMedicamento = idMedicamento;
        this.nombreMedicamento = nombreMedicamento;
        this.dosis = dosis;
        this.duracion = duracion;
    }

    public int getId()                  { return id; }
    public int getIdReceta()            { return idReceta; }
    public String getIdMedicamento()    { return idMedicamento; }
    public String getNombreMedicamento(){ return nombreMedicamento; }
    public String getDosis()            { return dosis; }
    public String getDuracion()         { return duracion; }

    public void setId(int id)                       { this.id = id; }
    public void setIdReceta(int idr)                { this.idReceta = idr; }
    public void setIdMedicamento(String idm)        { this.idMedicamento = idm; }
    public void setNombreMedicamento(String n)      { this.nombreMedicamento = n; }
    public void setDosis(String dosis)              { this.dosis = dosis; }
    public void setDuracion(String duracion)        { this.duracion = duracion; }
}
