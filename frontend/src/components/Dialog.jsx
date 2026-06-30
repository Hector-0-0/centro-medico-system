import { createContext, useContext, useState } from 'react';

/**
 * Diálogos con el estilo guinda/crema en vez de los `alert()`/`confirm()`
 * nativos del navegador. Uso:
 *
 *   const { alerta, confirmar } = useDialog();
 *   await alerta('Mensaje informativo');
 *   if (await confirmar('¿Eliminar?')) { ... }
 *
 * Reutiliza las clases `.modal*` y `.btn*` de Layout.css.
 */
const DialogContext = createContext(null);

const ESTADO_CERRADO = { abierto: false };

export function DialogProvider({ children }) {
  const [dlg, setDlg] = useState(ESTADO_CERRADO);

  const cerrar = (valor) => {
    dlg.resolver?.(valor);
    setDlg(ESTADO_CERRADO);
  };

  const alerta = (mensaje, { titulo = 'Aviso', textoOk = 'Aceptar' } = {}) =>
    new Promise((resolver) => {
      setDlg({ abierto: true, tipo: 'alerta', mensaje, titulo, textoOk, resolver });
    });

  const confirmar = (mensaje, { titulo = 'Confirmar', textoOk = 'Aceptar', textoCancelar = 'Cancelar', peligro = false } = {}) =>
    new Promise((resolver) => {
      setDlg({ abierto: true, tipo: 'confirmar', mensaje, titulo, textoOk, textoCancelar, peligro, resolver });
    });

  return (
    <DialogContext.Provider value={{ alerta, confirmar }}>
      {children}
      {dlg.abierto && (
        <div className="modal__overlay" onClick={() => cerrar(dlg.tipo === 'confirmar' ? false : undefined)}>
          <div className="modal" onClick={(e) => e.stopPropagation()} style={{ maxWidth: 420 }}>
            <div className="modal__header">{dlg.titulo}</div>
            <div className="modal__body">
              <p style={{ margin: 0, whiteSpace: 'pre-line' }}>{dlg.mensaje}</p>
            </div>
            <div className="modal__footer">
              {dlg.tipo === 'confirmar' && (
                <button className="btn btn--ghost" onClick={() => cerrar(false)}>
                  {dlg.textoCancelar}
                </button>
              )}
              <button
                className="btn btn--primary"
                onClick={() => cerrar(dlg.tipo === 'confirmar' ? true : undefined)}
                autoFocus
              >
                {dlg.textoOk}
              </button>
            </div>
          </div>
        </div>
      )}
    </DialogContext.Provider>
  );
}

export function useDialog() {
  const ctx = useContext(DialogContext);
  if (!ctx) throw new Error('useDialog debe usarse dentro de <DialogProvider>');
  return ctx;
}
