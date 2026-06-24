package pe.edu.uni.centromedico.ui.components;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchBar extends javax.swing.JPanel {

    private final javax.swing.JTextField txtBuscar;
    private final List<Runnable> listeners = new ArrayList<>();

    public SearchBar(String placeholder) {
        setLayout(new net.miginfocom.swing.MigLayout("fill, insets 0", "[grow]", "[36!]"));
        setOpaque(false);

        txtBuscar = new javax.swing.JTextField();
        txtBuscar.putClientProperty("JTextField.placeholderText", placeholder);
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { notificar(); }
            @Override public void removeUpdate(DocumentEvent e)  { notificar(); }
            @Override public void changedUpdate(DocumentEvent e) { notificar(); }
            private void notificar() {
                for (Runnable r : listeners) r.run();
            }
        });
        add(txtBuscar, "growx, h 36!");
    }

    public String getTexto() {
        return txtBuscar.getText().trim();
    }

    public void addSearchListener(Runnable listener) {
        listeners.add(listener);
    }

    public javax.swing.JTextField getTextField() {
        return txtBuscar;
    }
}
