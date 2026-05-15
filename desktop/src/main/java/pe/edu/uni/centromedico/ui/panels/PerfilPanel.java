package pe.edu.uni.centromedico.ui.panels;


import pe.edu.uni.centromedico.models.*;

public class PerfilPanel extends javax.swing.JPanel {

    public PerfilPanel(Persona persona) {
        initComponents();

        // Card izquierdo: datos personales
        pnl_card_izq.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 30", "[grow, center]",
                "[]14[]6[]6[]10[]6[]6[]20[]"));
        pnl_card_izq.removeAll();
        pnl_card_izq.add(lbl_avatar, "center, w 80!, h 80!, wrap");
        pnl_card_izq.add(lbl_nombre_perfil, "center, wrap");
        pnl_card_izq.add(lbl_codigo_perfil, "center, wrap");
        pnl_card_izq.add(lbl_especialidad, "center, wrap");
        pnl_card_izq.add(sep_perfil, "growx, wrap");
        pnl_card_izq.add(lbl_email_perfil, "center, wrap");
        pnl_card_izq.add(lbl_tel, "center, wrap");
        pnl_card_izq.add(btn_editar_perfil, "center, h 38!, w 160!");
        lbl_nombre_perfil.setText(persona.getNombre());
        lbl_codigo_perfil.setText("Código: " + persona.getId());
        if (persona instanceof Estudiante) {
            Estudiante e = (Estudiante) persona;
            lbl_especialidad.setText("Carrera: " + e.getCarrera());
        } else if (persona instanceof Doctor) {
            Doctor d = (Doctor) persona;
            lbl_especialidad.setText("Especialidad: " + d.getEspecialidad());
        }
        // Card derecho: estadísticas
        pnl_card_der.setLayout(new net.miginfocom.swing.MigLayout(
                "fillx, insets 30", "[grow]", "[]20[]8[]8[]"));
        pnl_card_der.removeAll();
        pnl_card_der.add(lbl_stats_titulo, "wrap");
        pnl_card_der.add(lbl_total_citas, "wrap");
        pnl_card_der.add(jLabel1, "wrap");
        pnl_card_der.add(lbl_atendidas, "wrap");

        // Layout principal: dos columnas iguales
        this.setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[grow][grow]", "[grow]"));
        this.removeAll();
        this.add(pnl_card_izq, "grow");
        this.add(pnl_card_der, "grow");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_card_izq = new javax.swing.JPanel();
        lbl_avatar = new javax.swing.JLabel();
        lbl_nombre_perfil = new javax.swing.JLabel();
        lbl_codigo_perfil = new javax.swing.JLabel();
        lbl_especialidad = new javax.swing.JLabel();
        sep_perfil = new javax.swing.JSeparator();
        lbl_email_perfil = new javax.swing.JLabel();
        lbl_tel = new javax.swing.JLabel();
        btn_editar_perfil = new javax.swing.JButton();
        pnl_card_der = new javax.swing.JPanel();
        lbl_stats_titulo = new javax.swing.JLabel();
        lbl_total_citas = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbl_atendidas = new javax.swing.JLabel();

        pnl_card_izq.setBackground(new java.awt.Color(255, 255, 255));

        lbl_avatar.setText("");

        lbl_nombre_perfil.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lbl_nombre_perfil.setText("Nombre Completo");

        lbl_codigo_perfil.setText("Código: U001");

        lbl_especialidad.setText("Ingeniería de Sistemas");

        lbl_email_perfil.setText("correo@uni.edu.pe");

        lbl_tel.setText("+51 999 888 777");

        btn_editar_perfil.setText("Editar Perfil");

        javax.swing.GroupLayout pnl_card_izqLayout = new javax.swing.GroupLayout(pnl_card_izq);
        pnl_card_izq.setLayout(pnl_card_izqLayout);
        pnl_card_izqLayout.setHorizontalGroup(
                pnl_card_izqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                .addGroup(pnl_card_izqLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addGroup(pnl_card_izqLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                                                .addComponent(lbl_avatar)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        108, Short.MAX_VALUE)
                                                                .addComponent(lbl_especialidad))
                                                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                                                .addComponent(lbl_nombre_perfil)
                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addComponent(lbl_codigo_perfil)))
                                .addGap(30, 30, 30))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_card_izqLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(sep_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(104, 104, 104))
                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                .addGroup(pnl_card_izqLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                                .addGap(96, 96, 96)
                                                .addComponent(lbl_email_perfil))
                                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(btn_editar_perfil)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lbl_tel)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pnl_card_izqLayout.setVerticalGroup(
                pnl_card_izqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_card_izqLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_email_perfil)
                                .addGap(14, 14, 14)
                                .addGroup(pnl_card_izqLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_avatar)
                                        .addComponent(lbl_especialidad))
                                .addGap(26, 26, 26)
                                .addComponent(lbl_nombre_perfil)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_codigo_perfil)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sep_perfil, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnl_card_izqLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbl_tel)
                                        .addComponent(btn_editar_perfil))
                                .addContainerGap(20, Short.MAX_VALUE)));

        pnl_card_der.setBackground(new java.awt.Color(255, 255, 255));

        lbl_stats_titulo.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        lbl_stats_titulo.setText("Resumen");

        lbl_total_citas.setText("Citas totales: 0");

        jLabel1.setText("Citas pendientes: 0");

        lbl_atendidas.setText("Citas atendidas: 0");

        javax.swing.GroupLayout pnl_card_derLayout = new javax.swing.GroupLayout(pnl_card_der);
        pnl_card_der.setLayout(pnl_card_derLayout);
        pnl_card_derLayout.setHorizontalGroup(
                pnl_card_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_card_derLayout.createSequentialGroup()
                                .addGroup(pnl_card_derLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_card_derLayout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(pnl_card_derLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnl_card_derLayout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(lbl_total_citas))
                                                        .addComponent(lbl_stats_titulo)))
                                        .addGroup(pnl_card_derLayout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addGroup(pnl_card_derLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbl_atendidas)
                                                        .addComponent(jLabel1))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pnl_card_derLayout.setVerticalGroup(
                pnl_card_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnl_card_derLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(lbl_stats_titulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_total_citas)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_atendidas)
                                .addContainerGap(17, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(pnl_card_izq, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnl_card_der, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(pnl_card_der, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pnl_card_izq, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(96, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_editar_perfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbl_atendidas;
    private javax.swing.JLabel lbl_avatar;
    private javax.swing.JLabel lbl_especialidad;
    private javax.swing.JLabel lbl_codigo_perfil;
    private javax.swing.JLabel lbl_email_perfil;
    private javax.swing.JLabel lbl_nombre_perfil;
    private javax.swing.JLabel lbl_stats_titulo;
    private javax.swing.JLabel lbl_tel;
    private javax.swing.JLabel lbl_total_citas;
    private javax.swing.JPanel pnl_card_der;
    private javax.swing.JPanel pnl_card_izq;
    private javax.swing.JSeparator sep_perfil;
    // End of variables declaration//GEN-END:variables
}
