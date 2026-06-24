package pe.edu.uni.centromedico.util;

import java.awt.Color;
import java.awt.Font;

public final class UIConstants {

    private UIConstants() {}

    // ── Colores ──────────────────────────────────────────────────────────
    public static final Color CARMESI       = new Color(0x71, 0x16, 0x10);
    public static final Color CARMESI_OSCURO = new Color(0x8B, 0x14, 0x14);
    public static final Color BLANCO        = Color.WHITE;
    public static final Color FONDO_PANEL   = new Color(0xF9, 0xF5, 0xF0);
    public static final Color FOOTER_BG     = new Color(0xF0, 0xEB, 0xE6);
    public static final Color BORDE_TABLA   = new Color(0xE8, 0xDD, 0xD8);
    public static final Color TEXTO_TITULO  = new Color(0x1E, 0x29, 0x3B);
    public static final Color SIDEBAR_HOVER = new Color(0xFF, 0xF1, 0xD3);
    public static final Color SIDEBAR_ACTIVO= new Color(0xFA, 0xED, 0xCF);

    // ── Alturas fijas ─────────────────────────────────────────────────────
    public static final int HEADER_HEIGHT  = 54;
    public static final int FOOTER_HEIGHT  = 50;
    public static final int INPUT_HEIGHT   = 36;
    public static final int BTN_ALTURA     = 38;
    public static final int ROW_HEIGHT     = 36;

    // ── Fuentes ───────────────────────────────────────────────────────────
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_BOTON  = new Font("Segoe UI", Font.BOLD, 13);

    // ── Estilos FlatLaf reutilizables ─────────────────────────────────────
    public static final String ESTILO_BTN_PRIMARIO =
        "arc: 8; borderWidth: 0; focusWidth: 0; innerFocusWidth: 0;"
        + " background: #711610; foreground: #ffffff;"
        + " hoverBackground: #711610; pressedBackground: #711610";

    public static final String ESTILO_BTN_SECUNDARIO =
        "arc: 8; borderWidth: 0; focusWidth: 0;"
        + " background: #888888; foreground: #ffffff;"
        + " hoverBackground: #888888; pressedBackground: #888888";

    // ── Métodos fábrica de botones ────────────────────────────────────────
    public static javax.swing.JButton crearBotonPrimario(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.putClientProperty("FlatLaf.style", ESTILO_BTN_PRIMARIO);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setFont(FUENTE_BOTON);
        return btn;
    }

    public static javax.swing.JButton crearBotonSecundario(String texto) {
        javax.swing.JButton btn = new javax.swing.JButton(texto);
        btn.putClientProperty("FlatLaf.style", ESTILO_BTN_SECUNDARIO);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.setFont(FUENTE_BOTON);
        return btn;
    }

    // ── Configuración de tabla estándar ───────────────────────────────────
    public static void configurarTabla(javax.swing.JTable tabla, javax.swing.JScrollPane scroll) {
        tabla.setRowHeight(ROW_HEIGHT);
        tabla.setColumnSelectionAllowed(false);
        tabla.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setResizable(false);
        }
        java.awt.Insets insets = scroll.getInsets();
        scroll.setViewportView(tabla);
        scroll.setBorder(javax.swing.BorderFactory.createLineBorder(BORDE_TABLA));
    }
}
