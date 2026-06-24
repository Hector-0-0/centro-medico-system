package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.db.dao.EstudianteDAO;
import pe.edu.uni.centromedico.models.Estudiante;
import pe.edu.uni.centromedico.util.UIConstants;
import java.util.List;

public class PacientePanel extends javax.swing.JPanel {

    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JTextField txt_buscar_pac;
    private final javax.swing.JButton btn_buscar_pac;
    private final javax.swing.JButton btn_nuevo_pac;
    private final javax.swing.JButton btn_eliminar_pac;
    private final javax.swing.JScrollPane scrl_pacientes;
    private final javax.swing.JTable tbl_pacientes;

    public PacientePanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Gestión de Pacientes");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);
        lbl_titulo.setForeground(UIConstants.TEXTO_TITULO);

        txt_buscar_pac = new javax.swing.JTextField();
        txt_buscar_pac.putClientProperty("JTextField.placeholderText", "Buscar por nombre...");

        btn_buscar_pac = new javax.swing.JButton("Buscar");
        btn_nuevo_pac = new javax.swing.JButton("+ Nuevo Paciente");
        btn_nuevo_pac.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_nuevo_pac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_eliminar_pac = new javax.swing.JButton("Eliminar");

        tbl_pacientes = new javax.swing.JTable();
        scrl_pacientes = new javax.swing.JScrollPane(tbl_pacientes);
        UIConstants.configurarTabla(tbl_pacientes, scrl_pacientes);

        cargarEstudiantes();

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]12[]12[grow]"));
        add(lbl_titulo, "wrap");

        javax.swing.JPanel pnlTop = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow][][]", "[36!]"));
        pnlTop.setOpaque(false);
        pnlTop.add(txt_buscar_pac, "growx, h 36!");
        pnlTop.add(btn_buscar_pac, "h 36!, gapleft 8");
        pnlTop.add(btn_nuevo_pac,  "h 36!, gapleft 4");
        add(pnlTop, "growx, wrap");
        add(scrl_pacientes, "grow");
    }

    private void cargarEstudiantes() {
        List<Estudiante> estudiantes = new EstudianteDAO().obtenerTodos();
        String[][] datos = new String[estudiantes.size()][5];
        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante e = estudiantes.get(i);
            datos[i][0] = e.getId();
            datos[i][1] = e.getNombre();
            datos[i][2] = String.valueOf(e.getEdad());
            datos[i][3] = e.getCarrera();
            datos[i][4] = e.getEmail();
        }
        tbl_pacientes.setModel(new javax.swing.table.DefaultTableModel(
                datos, new String[]{"Código", "Nombre", "Edad", "Carrera", "Email"}) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    public javax.swing.JTable   getTblPacientes()   { return tbl_pacientes; }
    public javax.swing.JButton  getBtnBuscar()       { return btn_buscar_pac; }
    public javax.swing.JButton  getBtnNuevo()        { return btn_nuevo_pac; }
    public javax.swing.JButton  getBtnEliminar()     { return btn_eliminar_pac; }
    public javax.swing.JTextField getTxtBuscar()     { return txt_buscar_pac; }
    public int getFilaSeleccionada()                 { return tbl_pacientes.getSelectedRow(); }
}
