//FFF1D3
package pe.edu.uni.centromedico.ui.components;

import java.awt.Color;
import javax.swing.*;

public class Sidebar extends javax.swing.JPanel {
    
    public Sidebar(String nombreUsuario, String rol) {
        initComponents();

        this.setBackground(new java.awt.Color(26, 10, 10));
        lblNombre.setText(nombreUsuario);

        this.setLayout(new net.miginfocom.swing.MigLayout(
            "fillx, insets 20 0 20 0", "[grow]",
            "[]10[]6[]10[]push[]"
        ));
        this.removeAll();
        this.add(lblLogo,     "center, wrap");
        this.add(lblCentro,   "center, wrap");
        this.add(lblNombre,   "center, wrap");
        this.add(separator,   "growx, wrap");

        // Botones dinámicos según rol
        agregarBotonesRol(rol);

        this.add(btnSalir, "growx, h 38!, gapleft 20, gapright 20");
    }

    private void agregarBotonesRol(String rol) {
        String[][] menus = switch (rol) {
            case "PACIENTE" -> new String[][]{
                {"Horarios",      "DASH"},
                {"Agendar Cita",  "AGENDAR"},
                {"Mis Citas",     "HISTORIAL"},
                {"Mi Perfil",     "PERFIL"}
            };
            case "MEDICO" -> new String[][]{
                {"Mis Citas",     "CITAS_MEDICO"},
                {"Disponibilidad","DISPONIBILIDAD"},
                {"Ver Stock",     "STOCK_VER"},
                {"Mi Perfil",     "PERFIL"}
            };
            case "ADMIN" -> new String[][]{
                {"Pacientes",     "PACIENTES"},
                {"Médicos",       "MEDICOS"},
                {"Todas las Citas","ADMIN_CITAS"}
            };
            case "FARMACIA" -> new String[][]{
                {"Recetas",       "RECETAS"},
                {"Gestión Stock", "STOCK_EDIT"}
            };
            default -> new String[][]{};
        };

        for (String[] m : menus) {
            javax.swing.JButton btn = crearBotonMenu(m[0]);
            final String destino = m[1];
            btn.addActionListener(e -> {
                pe.edu.uni.centromedico.ui.frames.MainFrame mf =
                    pe.edu.uni.centromedico.ui.frames.MainFrame.getInstance();
                if (mf == null) return;
                switch (destino) {
                    case "DASH"          -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.DashboardPanel(),         "Horarios Disponibles");
                    case "AGENDAR"       -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.DashboardPanel(),         "Agendar Cita");
                    case "HISTORIAL"     -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.HistorialPanel(),          "Mis Citas");
                    case "PERFIL"        -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.PerfilPanel(),             "Mi Perfil");
                    case "CITAS_MEDICO"  -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.CitaPanel(),              "Mis Citas");
                    case "DISPONIBILIDAD"-> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.DisponibilidadPanel(),    "Mi Disponibilidad");
                    case "STOCK_VER"     -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.MedicamentoPanel(),        "Stock");
                    case "PACIENTES"     -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.PacientePanel(),           "Pacientes");
                    case "MEDICOS"       -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.MedicoPanel(),             "Médicos");
                    case "ADMIN_CITAS"   -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.AdminCitasPanel(),         "Todas las Citas");
                    case "RECETAS"       -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.FarmaciaRecetasPanel(),    "Recetas Pendientes");
                    case "STOCK_EDIT"    -> mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.GestionStockPanel(),       "Gestión de Stock");
                }
            });
            this.add(btn, "growx, h 42!, gapleft 20, gapright 20, wrap 4");
        }
    }

    private javax.swing.JButton crearBotonMenu(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.setBackground(new java.awt.Color(26, 10, 10));
        btn.setForeground(new java.awt.Color(200, 200, 200));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setFont(new java.awt.Font("Liberation Sans", java.awt.Font.BOLD, 13));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogo = new javax.swing.JLabel();
        lblCentro = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        btnSalir = new javax.swing.JButton();

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unii2.png"))); // NOI18N

        lblCentro.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCentro.setForeground(new java.awt.Color(255, 255, 255));
        lblCentro.setText("Centro Médico UNI");

        lblNombre.setFont(new java.awt.Font("Segoe UI Emoji", 2, 15)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombre del estudiante");

        separator.setForeground(new java.awt.Color(55, 30, 255));

        btnSalir.setBackground(new java.awt.Color(139, 20, 20));
        btnSalir.setFont(new java.awt.Font("Segoe UI Emoji", 0, 15)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(255, 255, 255));
        btnSalir.setText("Cerrar Sesión");
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        btnSalir.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnSalirMouseMoved(evt);
            }
        });
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalirMouseExited(evt);
            }
        });
        btnSalir.addActionListener(e->{
            pe.edu.uni.centromedico.ui.frames.MainFrame mf = pe.edu.uni.centromedico.ui.frames.MainFrame.getInstance();
            if (mf != null) {
                mf.dispose();
                new pe.edu.uni.centromedico.ui.frames.LoginFrame().setVisible(true);
            }
        }); 

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addComponent(lblLogo))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(63, 63, 63)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblNombre)
                                .addComponent(lblCentro)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(130, 130, 130)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btnSalir)))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblLogo)
                .addGap(29, 29, 29)
                .addComponent(lblCentro)
                .addGap(18, 18, 18)
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158)
                .addComponent(btnSalir)
                .addGap(0, 54, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseMoved
        btnSalir.setBorder(null);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setOpaque(true);
        btnSalir.setBackground(Color.decode("#FFF1D3"));
        btnSalir.setForeground(Color.decode("#8b1414"));
        btnSalir.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
    }//GEN-LAST:event_btnSalirMouseMoved

    private void btnSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseExited
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBackground(Color.decode("#8b1414"));
        btnSalir.setForeground(Color.white);
        btnSalir.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
    }//GEN-LAST:event_btnSalirMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblCentro;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JSeparator separator;
    // End of variables declaration//GEN-END:variables

}