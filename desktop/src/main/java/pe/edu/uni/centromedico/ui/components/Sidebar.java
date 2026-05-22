//FFF1D3
package pe.edu.uni.centromedico.ui.components;

import java.awt.Color;
import javax.swing.*;
import pe.edu.uni.centromedico.models.Persona;

public class Sidebar extends javax.swing.JPanel {
    
    public Sidebar(Persona persona) {
        initComponents();

        this.setBackground(Color.decode("#8B1414"));
        lblNombre.setText(persona.name);
        lblNombre.setForeground(Color.WHITE);

        this.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 20 20 20 20", "[grow]", ""));
        lblNombre.setText(persona.getNombre());

        this.removeAll();
        this.add(lblLogo,     "center, wrap");
        this.add(lblCentro,   "center, wrap");
        this.add(lblNombre,"growx, center,wrap");
        this.add(separator,"center, wrap");

        // Botones dinámicos según rol
        agregarBotonesRol(persona);
        this.add(btnSalir, "growx, h 38!, pushy, aligny bottom");

        this.add(btnSalir, "growx, h 38!, gapleft 20, gapright 20");

        btnSalir.addActionListener(e -> {
            pe.edu.uni.centromedico.util.SesionManager.cerrar();
            javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
            new pe.edu.uni.centromedico.ui.frames.LoginFrame().setVisible(true);
        });
    }

    private void agregarBotonesRol(Persona persona) {
        String[][] menus = switch (persona.getRol()) {
            case "ESTUDIANTE" -> new String[][]{
                {"Horarios",      "DASH"},
                {"Mis Citas",     "HISTORIAL"},
                {"Mi Perfil",     "PERFIL"}
            };
            case "DOCTOR" -> new String[][]{
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
                // MVC: cada case crea View + Controller juntos
                switch (destino) {
                    case "DASH", "AGENDAR" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.DashboardPanel(persona);
                        new pe.edu.uni.centromedico.controller.DashboardController(p);
                        mf.mostrarPanel(p, "Horarios Disponibles");
                    }
                    case "HISTORIAL" -> {
                        var p = new pe.edu.uni.centromedico.ui.panels.HistorialPanel();
                        new pe.edu.uni.centromedico.controller.HistorialController(p);
                        mf.mostrarPanel(p, "Mis Citas");
                    }
                    case "PERFIL" ->
                        mf.mostrarPanel(new pe.edu.uni.centromedico.ui.panels.PerfilPanel(persona), "Mi Perfil");
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
            });
            this.add(btn, "growx, h 42!, wrap 5");
        }
    }

    private javax.swing.JButton crearBotonMenu(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.setBackground(Color.decode("#8B1414"));       // ← antes era (26,10,10)
        btn.setForeground(new java.awt.Color(200, 200, 200));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 16));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // ← AGREGAR estos listeners:
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
    


private JButton botonActivo = null; // ← AGREGAR como campo de la clase

private void setBotonActivo(JButton activo) {
    // Resetear el anterior
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

private void MovedBtn(JButton activo){
    activo.setBackground(Color.decode("#FFF1D3"));
    activo.setForeground(Color.decode("#8B1414"));
    activo.setBorderPainted(false);
    activo.setFocusPainted(false);
    activo.setOpaque(true);
    activo.setContentAreaFilled(true);  // ← AGREGAR
    activo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
}
private void ExitedBtn(JButton activo) {
    if (activo.getBackground().getRGB() != Color.decode("#FAEDCF").getRGB()) {
        activo.setBackground(Color.decode("#8B1414"));
        activo.setForeground(Color.decode("#F9F5F0"));
        activo.setContentAreaFilled(false);  // ← AGREGAR
        activo.setOpaque(false);
        activo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));
    }
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

    btnSalir.setBorderPainted(false);
    btnSalir.setFocusPainted(false);
    btnSalir.setOpaque(true);
    btnSalir.setContentAreaFilled(true);  // ← AGREGAR
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