package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.AtencionDAO;
import pe.edu.uni.centromedico.db.dao.CodigoCieDAO;
import pe.edu.uni.centromedico.db.dao.CitaDAO;
import pe.edu.uni.centromedico.models.*;
import java.util.List;

public class DetalleCitaDialog extends javax.swing.JDialog {

    private final Cita cita;

    public DetalleCitaDialog(java.awt.Frame parent, boolean modal, Cita cita) {
        super(parent, modal);
        this.cita = cita;
        setTitle("Detalle de Cita");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(500, 520));
        setResizable(false);
        setModal(true);

        getContentPane().setLayout(new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

        // Header
        javax.swing.JPanel header = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        header.setBackground(new java.awt.Color(139, 20, 20));
        javax.swing.JLabel titulo = new javax.swing.JLabel("Detalle de Cita");
        titulo.setForeground(java.awt.Color.WHITE);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        header.add(titulo, "align left");
        header.setPreferredSize(new java.awt.Dimension(0, 54));
        getContentPane().add(header, "growx, wrap");

        // Body
        javax.swing.JPanel body = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fillx, insets 20", "[right][grow]", "[]8[]8[]8[]8[]8[]8[]16[]"));
        body.setBackground(java.awt.Color.WHITE);

        String estado = cita.getEstado() != null ? cita.getEstado() : "—";
        String hora = (cita.getHoraInicio() != null ? cita.getHoraInicio() : "")
                    + (cita.getHoraFin() != null ? " - " + cita.getHoraFin() : "");

        body.add(new javax.swing.JLabel("Especialidad:"), "right");
        body.add(new javax.swing.JLabel(cita.getEspecialidad() != null ? cita.getEspecialidad() : "—"), "wrap");

        body.add(new javax.swing.JLabel("Médico:"), "right");
        body.add(new javax.swing.JLabel(cita.getNombreDoctor() != null ? cita.getNombreDoctor() : cita.getIdDoctor()), "wrap");

        body.add(new javax.swing.JLabel("Día:"), "right");
        body.add(new javax.swing.JLabel(cita.getDiaSemana() != null ? cita.getDiaSemana() : "—"), "wrap");

        body.add(new javax.swing.JLabel("Hora:"), "right");
        body.add(new javax.swing.JLabel(hora), "wrap");

        if (cita.getFechaCreacion() != null) {
            body.add(new javax.swing.JLabel("Registrada:"), "right");
            body.add(new javax.swing.JLabel(cita.getFechaCreacion()), "wrap");
        }

        body.add(new javax.swing.JLabel("Motivo:"), "right");
        body.add(new javax.swing.JLabel(cita.getMotivo() != null ? cita.getMotivo() : "—"), "wrap");

        body.add(new javax.swing.JLabel("Estado:"), "right");
        javax.swing.JLabel lblEstado = new javax.swing.JLabel(estado);
        lblEstado.setOpaque(true);
        java.awt.Color colorEstado = switch (estado) {
            case "ATENDIDA" -> new java.awt.Color(40, 180, 70);
            case "PENDIENTE" -> new java.awt.Color(220, 160, 20);
            case "CANCELADA" -> new java.awt.Color(180, 60, 60);
            case "NO_ASISTIO" -> new java.awt.Color(160, 80, 80);
            default -> java.awt.Color.GRAY;
        };
        lblEstado.setBackground(colorEstado);
        lblEstado.setForeground(java.awt.Color.WHITE);
        lblEstado.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblEstado.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10));
        body.add(lblEstado, "wrap");

        // Diagnostic info
        if ("ATENDIDA".equals(estado)) {
            body.add(new javax.swing.JLabel("Diagnósticos:"), "right, top");
            AtencionCita atencion = new AtencionDAO().obtenerPorCitaId(cita.getId());
            javax.swing.JPanel diagPanel = new javax.swing.JPanel(
                    new net.miginfocom.swing.MigLayout("fillx, insets 0", "[grow]", "[]4[]"));
            diagPanel.setBackground(java.awt.Color.WHITE);

            if (atencion != null) {
                List<AtencionDiagnostico> diags = new CodigoCieDAO().obtenerPorAtencion(atencion.getId());
                if (diags != null && !diags.isEmpty()) {
                    javax.swing.JTextArea ta = new javax.swing.JTextArea(3, 30);
                    ta.setEditable(false);
                    ta.setBackground(new java.awt.Color(245, 245, 245));
                    StringBuilder sb = new StringBuilder();
                    for (AtencionDiagnostico d : diags) {
                        sb.append("• ").append(d.getCodigoCie())
                          .append(" - ").append(d.getDescripcionCie());
                        if (d.getObservacion() != null && !d.getObservacion().isEmpty()) {
                            sb.append(" (").append(d.getObservacion()).append(")");
                        }
                        sb.append("\n");
                    }
                    ta.setText(sb.toString());
                    diagPanel.add(ta, "growx, wrap");
                }
                if (atencion.getComentarios() != null && !atencion.getComentarios().isEmpty()) {
                    diagPanel.add(new javax.swing.JLabel("Comentarios:"), "wrap");
                    javax.swing.JTextArea taCom = new javax.swing.JTextArea(atencion.getComentarios(), 2, 30);
                    taCom.setEditable(false);
                    taCom.setBackground(new java.awt.Color(245, 245, 245));
                    diagPanel.add(taCom, "growx, wrap");
                }
            }

            if (diagPanel.getComponentCount() == 0) {
                diagPanel.add(new javax.swing.JLabel("(Sin diagnóstico registrado)"));
            }
            body.add(diagPanel, "growx, wrap");

        } else if ("PENDIENTE".equals(estado)) {
            javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar Cita");
            btnCancelar.setBackground(new java.awt.Color(180, 60, 60));
            btnCancelar.setForeground(java.awt.Color.WHITE);
            btnCancelar.addActionListener(e -> cancelarCita());
            body.add(btnCancelar, "span 2, center, wrap");
        }

        getContentPane().add(body, "grow, push, wrap");

        // Footer
        javax.swing.JPanel footer = new javax.swing.JPanel(
                new net.miginfocom.swing.MigLayout("fillx, insets 10 20 10 20", "[grow][][]", "[38!]"));
        footer.setBackground(new java.awt.Color(240, 235, 230));

        javax.swing.JButton btnCerrar = new javax.swing.JButton("Cerrar");
        btnCerrar.setBackground(new java.awt.Color(139, 20, 20));
        btnCerrar.setForeground(java.awt.Color.WHITE);
        btnCerrar.addActionListener(e -> dispose());
        footer.add(btnCerrar, "tag ok");
        footer.setPreferredSize(new java.awt.Dimension(0, 50));
        getContentPane().add(footer, "growx");

        pack();
        setLocationRelativeTo(parent);
    }

    private void cancelarCita() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar esta cita?", "Confirmar cancelación",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        boolean ok = new CitaDAO().cancelarCita(cita.getId());
        if (ok) {
            cita.setEstado("CANCELADA");
            javax.swing.JOptionPane.showMessageDialog(this, "Cita cancelada exitosamente.");
            dispose();
        } else {
            new ErrorDialog(null, true, "Error al cancelar la cita.").setVisible(true);
        }
    }
}
