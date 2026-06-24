package pe.edu.uni.centromedico.ui.dialogs;

import pe.edu.uni.centromedico.db.dao.MedicamentoDAO;
import pe.edu.uni.centromedico.models.Medicamento;
import pe.edu.uni.centromedico.util.UIConstants;
import pe.edu.uni.centromedico.util.Validador;

public class NuevoMedicamentoDialog extends javax.swing.JDialog {

    private final javax.swing.JTextField txtId;
    private final javax.swing.JTextField txtNombre;
    private final javax.swing.JTextField txtStock;
    private final javax.swing.JComboBox<String> comboTipo;
    private final javax.swing.JTextField txtDosis;
    private final javax.swing.JLabel lblError;

    public NuevoMedicamentoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Nuevo Medicamento");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);

        getContentPane().setLayout(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 0", "[grow]", "[54!][grow,fill][50!]"));

        javax.swing.JPanel header = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout("fill, insets 0 20 0 20", "[grow]", "[center]"));
        header.setBackground(UIConstants.CARMESI);
        javax.swing.JLabel titulo = new javax.swing.JLabel("Nuevo Medicamento");
        titulo.setForeground(UIConstants.BLANCO);
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        header.add(titulo, "align left");
        header.setPreferredSize(new java.awt.Dimension(0, UIConstants.HEADER_HEIGHT));

        javax.swing.JPanel form = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fill, insets 24", "[120!][grow]", "[]8[]8[]8[]8[]8[]0[]"));

        txtId     = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtStock  = new javax.swing.JTextField("0");
        comboTipo = new javax.swing.JComboBox<>(new String[]{
            "comprimidos", "capsulas", "tabletas", "jarabe", "inyectable", "crema", "gotas"
        });
        comboTipo.setSelectedIndex(-1);
        txtDosis = new javax.swing.JTextField();
        txtDosis.putClientProperty("JTextField.placeholderText", "Ej: 500mg");
        lblError = new javax.swing.JLabel("");
        lblError.setForeground(new java.awt.Color(200, 0, 0));
        lblError.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

        form.add(new javax.swing.JLabel("Código *"), "growx");
        form.add(txtId, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        form.add(new javax.swing.JLabel("Nombre *"), "growx");
        form.add(txtNombre, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        form.add(new javax.swing.JLabel("Stock *"), "growx");
        form.add(txtStock, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        form.add(new javax.swing.JLabel("Tipo *"), "growx");
        form.add(comboTipo, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        form.add(new javax.swing.JLabel("Dosis"), "growx");
        form.add(txtDosis, "growx, h " + UIConstants.INPUT_HEIGHT + "!, wrap");
        form.add(lblError, "span, growx, wrap");

        javax.swing.JPanel footer = new javax.swing.JPanel(
            new net.miginfocom.swing.MigLayout(
                "fillx, insets 10 20 10 20", "[grow][][]", "[" + UIConstants.BTN_ALTURA + "!]"));
        footer.setBackground(UIConstants.FOOTER_BG);
        footer.add(new javax.swing.JLabel(), "growx");

        javax.swing.JButton btnCancelar = new javax.swing.JButton("Cancelar");
        btnCancelar.putClientProperty("FlatLaf.style",
            "arc: 8; borderWidth: 0; focusWidth: 0;"
            + " background: #888888; foreground: #ffffff;"
            + " hoverBackground: #888888; pressedBackground: #888888");
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());
        footer.add(btnCancelar);

        javax.swing.JButton btnGuardar = new javax.swing.JButton("Guardar");
        btnGuardar.putClientProperty("FlatLaf.style",
            "arc: 8; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0;"
            + " background: #711610; foreground: #ffffff;"
            + " hoverBackground: #711610; pressedBackground: #711610");
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(e -> guardar());
        footer.add(btnGuardar);

        getContentPane().add(header, "growx, wrap");
        getContentPane().add(form, "grow, push, wrap");
        getContentPane().add(footer, "growx");
        header.setPreferredSize(new java.awt.Dimension(0, UIConstants.HEADER_HEIGHT));
        footer.setPreferredSize(new java.awt.Dimension(0, UIConstants.FOOTER_HEIGHT));

        setMinimumSize(new java.awt.Dimension(460, 480));
        pack();
        setLocationRelativeTo(parent);
    }

    private void guardar() {
        lblError.setText("");
        String id     = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo   = comboTipo.getSelectedItem() != null
            ? comboTipo.getSelectedItem().toString() : "";
        String stockStr = txtStock.getText().trim();
        String dosis   = txtDosis.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || tipo.isEmpty()) {
            lblError.setText("Código, nombre y tipo son obligatorios.");
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException ex) {
            lblError.setText("El stock debe ser un número entero.");
            return;
        }

        String errStock = Validador.validarStock(stock);
        if (errStock != null) {
            lblError.setText(errStock);
            return;
        }

        if (!dosis.isEmpty()) {
            String errDosis = Validador.validarDosis(dosis);
            if (errDosis != null) {
                lblError.setText(errDosis);
                return;
            }
        }

        Medicamento med = new Medicamento(id, nombre, stock, tipo);
        med.setDosis(dosis);

        boolean ok = new MedicamentoDAO().registrar(med);
        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Medicamento registrado correctamente.",
                "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            lblError.setText("Error al registrar. El código ya puede existir.");
        }
    }
}
