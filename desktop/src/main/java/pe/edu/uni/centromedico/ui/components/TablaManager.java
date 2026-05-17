package pe.edu.uni.centromedico.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.function.Function;

public class TablaManager<T> {

    private final JTable tabla;
    private final String[] columnas;
    private final List<Function<T, Object>> extractores;
    private DefaultTableModel modelo;
    private List<T> datosActuales;

    /**
     * @param tabla       la JTable del panel
     * @param columnas    nombres de las columnas: {"Nombre", "Edad", ...}
     * @param extractores funciones que extraen el valor de cada columna del objeto
     *
     * Ejemplo de uso:
     * TablaManager<Estudiante> tm = new TablaManager<>(
     *     tbl_estudiantes,
     *     new String[]{"ID", "Nombre", "Carrera", "Email"},
     *     List.of(
     *         Estudiante::getId,
     *         Estudiante::getNombre,
     *         Estudiante::getCarrera,
     *         Estudiante::getEmail
     *     )
     * );
     */
    public TablaManager(JTable tabla, String[] columnas,
                        List<Function<T, Object>> extractores) {
        this.tabla = tabla;
        this.columnas = columnas;
        this.extractores = extractores;
        inicializarModelo();
        configurarTabla();
    }

    // ── Setup inicial ────────────────────────────────────────────────────────

    private void inicializarModelo() {
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ninguna celda editable
            }
        };
        tabla.setModel(modelo);
    }

    private void configurarTabla() {
        tabla.setRowHeight(36);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setColumnSelectionAllowed(false);

        // Deshabilitar resize de columnas
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setResizable(false);
        }

        // Ordenamiento por click en header
        tabla.setRowSorter(new TableRowSorter<>(modelo));
    }

    // ── Carga de datos ───────────────────────────────────────────────────────

    /**
     * Carga una lista de objetos en la tabla.
     * Reemplaza todos los datos anteriores.
     */
    public void cargar(List<T> datos) {
        this.datosActuales = datos;
        modelo.setRowCount(0); // limpiar filas anteriores
        for (T item : datos) {
            Object[] fila = new Object[extractores.size()];
            for (int i = 0; i < extractores.size(); i++) {
                fila[i] = extractores.get(i).apply(item);
            }
            modelo.addRow(fila);
        }
    }

    /**
     * Filtra los datos actuales según un texto de búsqueda.
     * Busca en todas las columnas.
     */
    public void filtrarPorTexto(String texto) {
        if (datosActuales == null) return;
        if (texto == null || texto.trim().isEmpty()) {
            cargar(datosActuales);
            return;
        }

        String textoBusqueda = texto.trim().toLowerCase();
        modelo.setRowCount(0);

        for (T item : datosActuales) {
            for (Function<T, Object> extractor : extractores) {
                Object valor = extractor.apply(item);
                if (valor != null && valor.toString()
                        .toLowerCase().contains(textoBusqueda)) {
                    Object[] fila = new Object[extractores.size()];
                    for (int i = 0; i < extractores.size(); i++) {
                        fila[i] = extractores.get(i).apply(item);
                    }
                    modelo.addRow(fila);
                    break; // evita duplicar la fila si coincide en varias columnas
                }
            }
        }
    }

    /**
     * Filtra los datos actuales con una condición personalizada.
     * Útil para filtros de disponibilidad, estado, etc.
     */
    public void filtrarPorCondicion(java.util.function.Predicate<T> condicion) {
        if (datosActuales == null) return;
        modelo.setRowCount(0);
        for (T item : datosActuales) {
            if (condicion.test(item)) {
                Object[] fila = new Object[extractores.size()];
                for (int i = 0; i < extractores.size(); i++) {
                    fila[i] = extractores.get(i).apply(item);
                }
                modelo.addRow(fila);
            }
        }
    }

    /**
     * Devuelve el objeto de la fila seleccionada en la tabla.
     * Retorna null si no hay selección.
     */
    public T getSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista == -1) return null;

        // Convertir índice de vista a índice del modelo (por si está ordenado)
        int filaModelo = tabla.convertRowIndexToModel(filaVista);
        return datosActuales.get(filaModelo);
    }

    /**
     * Limpia la tabla y los datos actuales.
     */
    public void limpiar() {
        modelo.setRowCount(0);
        datosActuales = null;
    }

    public DefaultTableModel getModelo() { return modelo; }
    public List<T> getDatosActuales()    { return datosActuales; }
}