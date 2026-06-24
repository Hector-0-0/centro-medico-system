package pe.edu.uni.centromedico.ui.components;

import java.awt.Color;
import javax.swing.*;
import pe.edu.uni.centromedico.models.Persona;
import pe.edu.uni.centromedico.util.ConfiguracionParametros;
import pe.edu.uni.centromedico.util.ErrorHandler;

public class Sidebar extends javax.swing.JPanel {

    private final ConfiguracionParametros OBJ = new ConfiguracionParametros();
    private final javax.swing.JLabel lblLogo;
    private final javax.swing.JLabel lblCentro;
    private final javax.swing.JLabel lblNombre;
    private final javax.swing.JSeparator separator;
    private final javax.swing.JButton btnSalir;
    private JButton botonActivo = null;

    public Sidebar(Persona persona) {
        lblLogo = new javax.swing.JLabel();
        lblCentro = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        btnSalir = new javax.swing.JButton();

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unii2.png")));

        lblCentro.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblCentro.setForeground(new java.awt.Color(255, 255, 255));
        lblCentro.setText("Centro Médico UNI");

        lblNombre.setFont(new java.awt.Font("Segoe UI Emoji", 2, 15));
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombre del estudiante");

        separator.setForeground(new java.awt.Color(55, 30, 255));

        btnSalir.setBackground(new java.awt.Color(139, 20, 20));
        btnSalir.setFont(new java.awt.Font("Segoe UI Emoji", 0, 15));
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("Cerrar Sesión");
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        ImageIcon logoIcon = OBJ.cargarIcono("unii2.png");
        if (logoIcon != null) lblLogo.setIcon(logoIcon);

        this.setBackground(Color.decode("#8B1414"));
        lblNombre.setText(persona.getNombre());
        lblNombre.setForeground(Color.WHITE);

        this.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 20 20 20 20", "[grow]", ""));
        lblNombre.setText(persona.getNombre());

        this.add(lblLogo, "center, wrap");
        this.add(lblCentro, "center, wrap");
        this.add(lblNombre, "center, wrap");
        this.add(separator, "center, wrap");

        agregarBotonesRol(persona);

        btnSalir.setHorizontalAlignment(SwingConstants.CENTER);
        btnSalir.setVerticalAlignment(SwingConstants.CENTER);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        btnSalir.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnSalir.setBorderPainted(false);
                btnSalir.setFocusPainted(false);
                btnSalir.setOpaque(true);
                btnSalir.setContentAreaFilled(true);
                btnSalir.setBackground(Color.decode("#FFF1D3"));
                btnSalir.setForeground(Color.decode("#8b1414"));
                btnSalir.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
            }
        });
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalir.setContentAreaFilled(false);
                btnSalir.setBackground(Color.decode("#8b1414"));
                btnSalir.setForeground(Color.white);
                btnSalir.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
            }
        });

        this.add(btnSalir, "w 140!, h 38!, pushx, alignx right, pushy, aligny bottom, gapright 15, gapbottom 15");
        btnSalir.addActionListener(e -> ErrorHandler.ejecutarSeguro(this, () -> {
            pe.edu.uni.centromedico.util.SesionManager.cerrar();
            javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
            new pe.edu.uni.centromedico.ui.frames.LoginFrame().setVisible(true);
        }));
    }

    private void agregarBotonesRol(Persona persona) {
        String[][] menus = switch (persona.getRol()) {
            case "ESTUDIANTE" -> new String[][]{
                {"Horarios", "DASH"},
                {"Mis Citas", "HISTORIAL"},
                {"Mi Perfil", "PERFIL"}
            };
            case "DOCTOR" -> new String[][]{
                {"Mis Citas", "CITAS_MEDICO"},
                {"Disponibilidad", "DISPONIBILIDAD"},
                {"Ver Stock", "STOCK_VER"},
                {"Mi Perfil", "PERFIL"}
            };
            case "ADMIN" -> new String[][]{
                {"Pacientes", "PACIENTES"},
                {"Médicos", "MEDICOS"},
                {"Todas las Citas", "ADMIN_CITAS"}
            };
            case "FARMACIA" -> new String[][]{
                {"Recetas", "RECETAS"},
                {"Gestión Stock", "STOCK_EDIT"}
            };
            default -> new String[][]{};
        };

        for (String[] m : menus) {
            javax.swing.JButton btn = crearBotonMenu(m[0]);
            final String destino = m[1];
            btn.addActionListener(e -> ErrorHandler.ejecutarSeguro(this, () -> {
                pe.edu.uni.centromedico.ui.frames.MainFrame mf =
                    pe.edu.uni.centromedico.ui.frames.MainFrame.getInstance();
                if (mf == null) return;
                switch (destino) {
                    case "DASH", "AGENDAR" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.DashboardPanel();
                        new pe.edu.uni.centromedico.controller.DashboardController(p);
                        mf.mostrarPanel(p, "Horarios Disponibles");
                    }
                    case "HISTORIAL" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.HistorialPanel();
                        new pe.edu.uni.centromedico.controller.HistorialController(p);
                        mf.mostrarPanel(p, "Mis Citas");
                    }
                    case "PERFIL" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.PerfilPanel(persona);
                        new pe.edu.uni.centromedico.controller.PerfilController(p, persona);
                        mf.mostrarPanel(p, "Mi Perfil");
                    }
                    case "CITAS_MEDICO" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.CitaPanel();
                        new pe.edu.uni.centromedico.controller.CitaController(p);
                        mf.mostrarPanel(p, "Mis Citas");
                    }
                    case "DISPONIBILIDAD" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.DisponibilidadPanel();
                        new pe.edu.uni.centromedico.controller.DisponibilidadController(p);
                        mf.mostrarPanel(p, "Mi Disponibilidad");
                    }
                    case "STOCK_VER" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.MedicamentoPanel();
                        new pe.edu.uni.centromedico.controller.MedicamentoController(p);
                        mf.mostrarPanel(p, "Stock de Medicamentos");
                    }
                    case "PACIENTES" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.PacientePanel();
                        new pe.edu.uni.centromedico.controller.PacienteController(p);
                        mf.mostrarPanel(p, "Pacientes");
                    }
                    case "MEDICOS" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.MedicoPanel();
                        new pe.edu.uni.centromedico.controller.MedicoController(p);
                        mf.mostrarPanel(p, "Médicos");
                    }
                    case "ADMIN_CITAS" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.AdminCitasPanel();
                        new pe.edu.uni.centromedico.controller.AdminCitasController(p);
                        mf.mostrarPanel(p, "Todas las Citas");
                    }
                    case "RECETAS" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.FarmaciaRecetasPanel();
                        new pe.edu.uni.centromedico.controller.FarmaciaController(p);
                        mf.mostrarPanel(p, "Recetas Pendientes");
                    }
                    case "STOCK_EDIT" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.GestionStockPanel();
                        new pe.edu.uni.centromedico.controller.GestionStockController(p);
                        mf.mostrarPanel(p, "Gestión de Stock");
                    }
                }
                setBotonActivo(btn);
            }));
            this.add(btn, "growx, h 42!, wrap 5");
        }
    }

    private javax.swing.JButton crearBotonMenu(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.setBackground(Color.decode("#8B1414"));
        btn.setForeground(new java.awt.Color(200, 200, 200));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 16));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                MovedBtn(btn);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ExitedBtn(btn);
            }
        });

        return btn;
    }

    private void setBotonActivo(JButton activo) {
        if (botonActivo != null && botonActivo != activo) {
            botonActivo.setBackground(Color.decode("#8B1414"));
            botonActivo.setForeground(Color.decode("#F9F5F0"));
            botonActivo.setContentAreaFilled(false);
            botonActivo.setOpaque(false);
            botonActivo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        }
        botonActivo = activo;
        activo.setBackground(Color.decode("#FAEDCF"));
        activo.setForeground(Color.decode("#8B1414"));
        activo.setContentAreaFilled(true);
        activo.setOpaque(true);
        activo.setBorderPainted(false);
        activo.setFocusPainted(false);
        activo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
    }

    private void MovedBtn(JButton activo) {
        activo.setBackground(Color.decode("#FFF1D3"));
        activo.setForeground(Color.decode("#8B1414"));
        activo.setBorderPainted(false);
        activo.setFocusPainted(false);
        activo.setOpaque(true);
        activo.setContentAreaFilled(true);
        activo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
    }

    private void ExitedBtn(JButton activo) {
        if (activo.getBackground().getRGB() != Color.decode("#FAEDCF").getRGB()) {
            activo.setBackground(Color.decode("#8B1414"));
            activo.setForeground(Color.decode("#F9F5F0"));
            activo.setContentAreaFilled(false);
            activo.setOpaque(false);
            activo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
        }
    }
}
