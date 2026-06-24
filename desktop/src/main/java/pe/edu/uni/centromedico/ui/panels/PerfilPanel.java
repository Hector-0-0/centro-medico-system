package pe.edu.uni.centromedico.ui.panels;

import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.models.*;
import java.util.List;

public class PerfilPanel extends javax.swing.JPanel {

    private final Persona persona;
    private final javax.swing.JPanel pnl_card_izq;
    private final javax.swing.JLabel lbl_avatar;
    private final javax.swing.JLabel lbl_nombre_perfil;
    private final javax.swing.JLabel lbl_codigo_perfil;
    private final javax.swing.JLabel lbl_especialidad;
    private final javax.swing.JSeparator sep_perfil;
    private final javax.swing.JLabel lbl_email_perfil;
    private final javax.swing.JLabel lbl_tel;
    private final javax.swing.JButton btn_editar_perfil;
    private final javax.swing.JPanel pnl_card_der;
    private final javax.swing.JLabel lbl_stats_titulo;
    private final javax.swing.JLabel lbl_total_citas;
    private final javax.swing.JLabel jLabel1;
    private final javax.swing.JLabel lbl_atendidas;

    public PerfilPanel(Persona persona) {
        if (persona == null)
            throw new IllegalArgumentException("PerfilPanel requiere una persona con sesión activa.");
        this.persona = persona;

        setBackground(new java.awt.Color(249, 245, 240));

        pnl_card_izq = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 30", "[grow, center]",
                "[]14[]6[]6[]10[]6[]6[]20[]"));
        pnl_card_izq.setBackground(java.awt.Color.WHITE);

        lbl_avatar = new javax.swing.JLabel("");
        lbl_nombre_perfil = new javax.swing.JLabel("Nombre Completo");
        lbl_nombre_perfil.setFont(new java.awt.Font("Liberation Sans", 1, 18));
        lbl_codigo_perfil = new javax.swing.JLabel("Código: " + persona.getId());
        lbl_especialidad = new javax.swing.JLabel("");
        sep_perfil = new javax.swing.JSeparator();
        lbl_email_perfil = new javax.swing.JLabel("");
        lbl_tel = new javax.swing.JLabel("");
        btn_editar_perfil = new javax.swing.JButton("Editar Perfil");

        lbl_nombre_perfil.setText(persona.getNombre());

        if (persona instanceof Estudiante e) {
            lbl_especialidad.setText("Carrera: " + e.getCarrera());
            lbl_email_perfil.setText(e.getEmail() != null ? e.getEmail() : "—");
            lbl_tel.setText("Edad: " + e.getEdad() + " años");
        } else if (persona instanceof Doctor d) {
            lbl_especialidad.setText("Especialidad: " + d.getEspecialidad());
            lbl_email_perfil.setText("Consultorio: " + d.getConsultorio());
            lbl_tel.setText(d.isActivo() ? "Estado: Activo" : "Estado: Inactivo");
        } else {
            lbl_email_perfil.setText("—");
            lbl_tel.setText("—");
        }

        pnl_card_izq.add(lbl_avatar, "center, w 80!, h 80!, wrap");
        pnl_card_izq.add(lbl_nombre_perfil, "center, wrap");
        pnl_card_izq.add(lbl_codigo_perfil, "center, wrap");
        pnl_card_izq.add(lbl_especialidad, "center, wrap");
        pnl_card_izq.add(sep_perfil, "growx, wrap");
        pnl_card_izq.add(lbl_email_perfil, "center, wrap");
        pnl_card_izq.add(lbl_tel, "center, wrap");
        pnl_card_izq.add(btn_editar_perfil, "center, h 38!, w 160!");

        // Estadísticas de citas
        CitaDAO citaDAO = new CitaDAO();
        String id = persona.getId();
        List<Cita> citas;
        if (persona instanceof Estudiante) citas = citaDAO.obtenerPorEstudiante(id);
        else if (persona instanceof Doctor) citas = citaDAO.obtenerPorDoctor(id);
        else citas = java.util.Collections.emptyList();

        long pendientes = citas.stream().filter(c -> "PENDIENTE".equals(c.getEstado())).count();
        long atendidas  = citas.stream().filter(c -> "ATENDIDA".equals(c.getEstado())).count();

        pnl_card_der = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fillx, insets 30", "[grow]", "[]20[]8[]8[]"));
        pnl_card_der.setBackground(java.awt.Color.WHITE);

        lbl_stats_titulo = new javax.swing.JLabel("Resumen");
        lbl_stats_titulo.setFont(new java.awt.Font("Liberation Sans", 1, 15));
        lbl_total_citas = new javax.swing.JLabel("Total de citas: " + citas.size());
        jLabel1 = new javax.swing.JLabel("Citas pendientes: " + pendientes);
        lbl_atendidas = new javax.swing.JLabel("Citas atendidas: " + atendidas);

        pnl_card_der.add(lbl_stats_titulo, "wrap");
        pnl_card_der.add(lbl_total_citas, "wrap");
        pnl_card_der.add(jLabel1, "wrap");
        pnl_card_der.add(lbl_atendidas, "wrap");

        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 16", "[grow][grow]", "[grow]"));
        add(pnl_card_izq, "grow");
        add(pnl_card_der, "grow");
    }

    public void actualizarDatos(Persona persona) {
        lbl_nombre_perfil.setText(persona.getNombre());
        if (persona instanceof Estudiante e) {
            lbl_especialidad.setText("Carrera: " + e.getCarrera());
            lbl_email_perfil.setText(e.getEmail() != null ? e.getEmail() : "—");
        } else if (persona instanceof Doctor d) {
            lbl_especialidad.setText("Especialidad: " + d.getEspecialidad());
            lbl_email_perfil.setText("Consultorio: " + d.getConsultorio());
        }
    }

    public javax.swing.JButton getBtnEditarPerfil() { return btn_editar_perfil; }
}
