package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.Medicamento;

public class NuevoMedicamentoDialog extends javax.swing.JDialog {

    public NuevoMedicamentoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Nuevo Medicamento");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        getContentPane().setLayout(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

        // ── Header ────────────────────────────────────────────────────────
        javax.swing.JPanel header = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        header.setBackground(new java.awt.Color(139, 20, 20));
        javax.swing.JLabel titulo = new javax.swing.JLabel("Nuevo Medicamento");
        titulo.setForeground(java.awt.Color.WHITE);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        header.add(titulo, "align left");

        // ── Formulario ────────────────────────────────────────────────────
        javax.swing.JPanel form = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[120!][grow]", "[]10[]10[]10[]"));

        javax.swing.JTextField txtId     = new javax.swing.JTextField();
        javax.swing.JTextField txtNombre = new javax.swing.JTextField();
        javax.swing.JTextField txtStock  = new javax.swing.JTextField("0");
        javax.swing.JTextField txtTipo   = new javax.swing.JTextField();

        form.add(new javax.swing.JLabel("Código"),  "growx"); form.add(txtId,     "growx, wrap");
        form.add(new javax.swing.JLabel("Nombre"),  "growx"); form.add(txtNombre, "growx, wrap");
        form.add(new javax.swing.JLabel("Stock"),   "growx"); form.add(txtStock,  "growx, wrap");
        form.add(new javax.swing.JLabel("Tipo"),    "growx"); form.add(txtTipo,   "growx, wrap");

        // ── Footer ────────────────────────────────────────────────────────
        javax.swing.JPanel footer = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fillx, insets 10 20 10 20", "[grow][][]", "[38!]"));
        footer.setBackground(new java.awt.Color(240, 235, 230));
        footer.add(new javax.swing.JLabel(), "growx");

        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        btnCancelar.setBackground(new java.awt.Color(139, 20, 20));
        btnCancelar.setForeground(java.awt.Color.WHITE);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        footer.add(btnCancelar);

        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
        btnGuardar.setBackground(new java.awt.Color(139, 20, 20));
        btnGuardar.setForeground(java.awt.Color.WHITE);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> {
            String id     = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tipo   = txtTipo.getText().trim();
            String stockStr = txtStock.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || tipo.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Código, nombre y tipo son obligatorios.",
                    "Campos requeridos", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            int stock;
            try {
                stock = Integer.parseInt(stockStr);
                if (stock < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "El stock debe ser un número entero ≥ 0.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            Medicamento med = new Medicamento(id, nombre, stock, tipo);
            boolean ok = new MedicamentoDAO().registrar(med);
            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Medicamento registrado correctamente.",
                    "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al registrar. El código ya puede existir.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        footer.add(btnGuardar);

        getContentPane().add(header, "growx, wrap");
        getContentPane().add(form,   "grow, push, wrap");
        getContentPane().add(footer, "growx");
        header.setPreferredSize(new java.awt.Dimension(0, 54));
        footer.setPreferredSize(new java.awt.Dimension(0, 50));

        pack();
        setLocationRelativeTo(parent);
    }
}
