package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.ui.components.TablaManager;
import pe.edu.uni.centromedico.models.Slot;
import pe.edu.uni.centromedico.db.dao.SlotDAO;
import pe.edu.uni.centromedico.util.UIConstants;
import java.util.List;

public class DashboardPanel extends javax.swing.JPanel {

    private TablaManager<Slot> tablaManager;
    private final javax.swing.JLabel lbl_titulo;
    private final javax.swing.JLabel lbl_subtitulo;
    private final javax.swing.JPanel pnl_filtros;
    private final javax.swing.JButton btn_Todos;
    private final javax.swing.JButton btn_disponibles;
    private final javax.swing.JButton btn_ocupados;
    private final javax.swing.JScrollPane scrl_horarios;
    private final javax.swing.JTable tbl_horarios;
    private final javax.swing.JButton btn_agendar;
    private final javax.swing.JComboBox<String> cbx_Especialidad;

    public DashboardPanel() {
        setBackground(UIConstants.FONDO_PANEL);

        lbl_titulo = new javax.swing.JLabel("Horarios Disponibles");
        lbl_titulo.setFont(UIConstants.FUENTE_TITULO);
        lbl_subtitulo = new javax.swing.JLabel("Selecciona un horario y agenda tu cita");

        pnl_filtros = new javax.swing.JPanel(new net.miginfocom.swing.MigLayout("insets 0, gapx 8", "[][][]", "[]"));
        pnl_filtros.setOpaque(false);

        cbx_Especialidad = new javax.swing.JComboBox<>();
        btn_Todos = new javax.swing.JButton("Todos");
        btn_disponibles = new javax.swing.JButton("Disponibles");
        btn_ocupados = new javax.swing.JButton("Ocupados");

        btn_Todos.addActionListener(e -> filtrar("Todos"));
        btn_disponibles.addActionListener(e -> filtrar("1"));
        btn_ocupados.addActionListener(e -> filtrar("0"));

        pnl_filtros.add(cbx_Especialidad);
        pnl_filtros.add(btn_Todos);
        pnl_filtros.add(btn_disponibles);
        pnl_filtros.add(btn_ocupados);

        tbl_horarios = new javax.swing.JTable();
        scrl_horarios = new javax.swing.JScrollPane(tbl_horarios);
        UIConstants.configurarTabla(tbl_horarios, scrl_horarios);

        tablaManager = new TablaManager<>(
            tbl_horarios,
            new String[]{"Especialidad", "Médico", "Día", "Hora", "Consultorio", "Estado"},
            List.of(
                Slot::getEspecialidad, Slot::getNombreDoctor, Slot::getDiaSemana,
                s -> s.getHoraInicio() + " - " + s.getHoraFin(),
                Slot::getConsultorio, s -> s.isDisponible() ? "Disponible" : "Ocupado"));

        List<Slot> datos = new SlotDAO().obtenerTodos();
        tablaManager.cargar(datos);
        cbx_Especialidad.addItem("Seleccione Especialidad");
        datos.stream().map(Slot::getEspecialidad).distinct().forEach(cbx_Especialidad::addItem);

        btn_agendar = new javax.swing.JButton("Agendar Cita");
        btn_agendar.putClientProperty("FlatLaf.style", UIConstants.ESTILO_BTN_PRIMARIO);
        btn_agendar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        cbx_Especialidad.addActionListener(e -> filtrar(ultimo));

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow]", "[]4[]12[]12[grow]16[]"));
        add(lbl_titulo, "wrap");
        add(lbl_subtitulo, "wrap");
        add(pnl_filtros, "wrap");
        add(scrl_horarios, "grow, wrap");
        add(btn_agendar, "right, h 44!, w 200!");
    }

    private String ultimo = "Todos";

    private void filtrar(String tipo) {
        String especialidad = (String) cbx_Especialidad.getSelectedItem();
        boolean todas = especialidad == null || especialidad.equals("Seleccione Especialidad");
        tablaManager.filtrarPorCondicion(s -> {
            boolean coincideEsp = todas || s.getEspecialidad().equals(especialidad);
            boolean coincideEstado = tipo.equals("Todos")
                || (tipo.equals("1") && s.isDisponible())
                || (tipo.equals("0") && !s.isDisponible());
            return coincideEsp && coincideEstado;
        });
    }

    public void cargarDatos(List<Slot> slots) {
        tablaManager.cargar(slots);
        cbx_Especialidad.removeAllItems();
        cbx_Especialidad.addItem("Seleccione Especialidad");
        slots.stream().map(Slot::getEspecialidad).distinct().forEach(cbx_Especialidad::addItem);
    }

    public javax.swing.JButton getBtnAgendar()          { return btn_agendar; }
    public javax.swing.JButton getBtnTodos()            { return btn_Todos; }
    public javax.swing.JButton getBtnDisponibles()      { return btn_disponibles; }
    public javax.swing.JButton getBtnOcupados()         { return btn_ocupados; }
    public javax.swing.JComboBox<String> getCbxEspecialidad() { return cbx_Especialidad; }
    public javax.swing.JTable getTblHorarios()          { return tbl_horarios; }
    public void filtrarPublico(String tipo)             { filtrar(tipo); }
}
