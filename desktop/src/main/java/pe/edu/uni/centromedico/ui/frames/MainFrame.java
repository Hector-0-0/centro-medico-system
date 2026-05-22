package pe.edu.uni.centromedico.ui.frames;
import pe.edu.uni.centromedico.models.Persona;
public class MainFrame extends javax.swing.JFrame {

    // ── Singleton ─────────────────────────────────────────────────────────
    private static MainFrame instance;
    public static MainFrame getInstance() { return instance; }

    // ── Estado interno ────────────────────────────────────────────────────
    private javax.swing.JPanel areaCentral;
    private javax.swing.JLabel lbl_titulo_topbar;
    public MainFrame(Persona persona) {
        initComponents();
        instance = this;

        // Sidebar dinámico según rol
        pe.edu.uni.centromedico.ui.components.Sidebar sidebar =
            new pe.edu.uni.centromedico.ui.components.Sidebar(persona);

        // TopBar
        javax.swing.JPanel topBar = crearTopBar(persona.getNombre(), persona.getRol());

        // Área central
        areaCentral = new javax.swing.JPanel();
        areaCentral.setBackground(new java.awt.Color(249, 245, 240));
        areaCentral.setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 0", "[grow]", "[grow]"));

        // Layout: sidebar fijo | [topbar / contenido]
        getContentPane().setLayout(new net.miginfocom.swing.MigLayout(
            "fill, insets 0, gap 0",
            "[280!][grow, fill]",
            "[54!][grow, fill]"
        ));
        getContentPane().removeAll();
        getContentPane().add(sidebar,    "spany 2, growy");
        getContentPane().add(topBar,     "growx, wrap");
        getContentPane().add(areaCentral,"grow");

        // Panel inicial según rol — Controller + View instanciados juntos (MVC)
        switch (persona.getRol()) {
            case "ESTUDIANTE" -> {
                pe.edu.uni.centromedico.ui.panels.DashboardPanel dash =
                    new pe.edu.uni.centromedico.ui.panels.DashboardPanel(persona);
                new pe.edu.uni.centromedico.controller.DashboardController(dash);
                mostrarPanel(dash, "Horarios Disponibles");
            }
            case "MEDICO" -> {
                pe.edu.uni.centromedico.ui.panels.CitaPanel citas =
                    new pe.edu.uni.centromedico.ui.panels.CitaPanel();
                new pe.edu.uni.centromedico.controller.CitaController(citas);
                mostrarPanel(citas, "Mis Citas");
            }
            case "ADMIN" -> {
                pe.edu.uni.centromedico.ui.panels.PacientePanel pacs =
                    new pe.edu.uni.centromedico.ui.panels.PacientePanel();
                new pe.edu.uni.centromedico.controller.PacienteController(pacs);
                mostrarPanel(pacs, "Gestión de Pacientes");
            }
            case "FARMACIA" -> {
                pe.edu.uni.centromedico.ui.panels.GestionStockPanel stock =
                    new pe.edu.uni.centromedico.ui.panels.GestionStockPanel();
                new pe.edu.uni.centromedico.controller.GestionStockController(stock);
                mostrarPanel(stock, "Gestión de Stock");
            }
        }

        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(900, 580));
        setTitle("Centro Médico UNI — " + persona.getNombre());
    }

    // ── Navegación ────────────────────────────────────────────────────────
    public void mostrarPanel(javax.swing.JPanel panel, String titulo) {
        areaCentral.removeAll();
        areaCentral.add(panel, "grow");
        if (lbl_titulo_topbar != null) lbl_titulo_topbar.setText(titulo);
        areaCentral.revalidate();
        areaCentral.repaint();
    }

    // ── TopBar ────────────────────────────────────────────────────────────
    private javax.swing.JPanel crearTopBar(String nombre, String rol) {
        javax.swing.JPanel bar = new javax.swing.JPanel();
        bar.setBackground(java.awt.Color.WHITE);
        bar.setBorder(javax.swing.BorderFactory.createMatteBorder(
            0, 0, 1, 0, new java.awt.Color(232, 221, 216)));
        bar.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 0 20 0 20", "[grow][][]", "[54!, center]"));

        lbl_titulo_topbar = new javax.swing.JLabel("Inicio");
        lbl_titulo_topbar.setFont(
            new java.awt.Font("Liberation Sans", java.awt.Font.BOLD, 16));
        lbl_titulo_topbar.setForeground(new java.awt.Color(30, 41, 59));

        javax.swing.JLabel lblNombre = new javax.swing.JLabel(nombre);
        lblNombre.setFont(
            new java.awt.Font("Liberation Sans", java.awt.Font.BOLD, 12));
        lblNombre.setForeground(new java.awt.Color(30, 41, 59));

        javax.swing.JLabel lblRol = new javax.swing.JLabel(" " + rol + " ");
        lblRol.setOpaque(true);
        lblRol.setBackground(new java.awt.Color(113, 22, 16));
        lblRol.setForeground(java.awt.Color.WHITE);
        lblRol.setFont(new java.awt.Font("Liberation Sans", java.awt.Font.BOLD, 11));
        lblRol.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 8, 3, 8));

        bar.add(lbl_titulo_topbar, "grow");
        bar.add(lblNombre, "");
        bar.add(lblRol, "gapleft 8");
        return bar;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
