
package pe.edu.uni.centromedico.ui.components;

import java.awt.Color;
import javax.swing.*;


public class TopBar extends javax.swing.JPanel {

    
    public TopBar(String nombreUsuario, String rol, ImageIcon foto) {
        initComponents();
        this.setLayout(new net.miginfocom.swing.MigLayout("fillx, insets 10 20 10 20", "[][grow][right]", "[54!, center]"));
        this.setBackground(Color.WHITE);
        
        lblRol.setText(rol);
        lblRol.setOpaque(true);
        lblRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblRol.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        aplicarColorRol(rol);
        
        lblNombre.setText(nombreUsuario);
        
        this.add(lblRol,    "cell 0 0");
        this.add(lblNombre, "cell 2 0");
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombre = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        lblNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblNombre.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        lblNombre.setText("jLabel1");
        add(lblNombre, java.awt.BorderLayout.PAGE_START);

        lblRol.setBackground(new java.awt.Color(255, 255, 255));
        lblRol.setFont(new java.awt.Font("Segoe UI Emoji", 0, 14)); // NOI18N
        lblRol.setText("jLabel1");
        add(lblRol, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRol;
    // End of variables declaration//GEN-END:variables

private void aplicarColorRol(String rol) {
        switch (rol.toUpperCase()) {
            case "ADMIN"    -> {lblRol.setBackground(new Color(139, 20, 20));
                                lblRol.setForeground(Color.white);}
            case "MEDICO"   -> lblRol.setBackground(new Color(26, 86, 160));
            case "PACIENTE" -> lblRol.setBackground(new Color(15, 110, 70));
            default         -> lblRol.setBackground(new Color(100, 100, 100));
        }
    
}
}
