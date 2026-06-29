/**
 * Fila única para el `<tbody>` de una tabla que representa su estado:
 * cargando, error (con botón de reintentar) o vacío. Centraliza el patrón
 * que antes estaba duplicado (y con `catch {}` silenciosos) en cada página.
 *
 *   <tbody>
 *     {cargando || error || filas.length === 0 ? (
 *       <FilaTablaEstado colSpan={5} cargando={cargando} error={error}
 *                        onReintentar={recargar} vacio="Sin citas" />
 *     ) : (
 *       filas.map(...)
 *     )}
 *   </tbody>
 */
export default function FilaTablaEstado({
  colSpan,
  cargando,
  error,
  onReintentar,
  vacio = 'Sin datos',
}) {
  if (cargando) {
    return (
      <tr>
        <td className="table__empty" colSpan={colSpan}>Cargando…</td>
      </tr>
    );
  }
  if (error) {
    return (
      <tr>
        <td className="table__empty table__empty--error" colSpan={colSpan}>
          {error}
          {onReintentar && (
            <button className="btn btn--ghost btn--sm" onClick={onReintentar} style={{ marginLeft: 10 }}>
              Reintentar
            </button>
          )}
        </td>
      </tr>
    );
  }
  return (
    <tr>
      <td className="table__empty" colSpan={colSpan}>{vacio}</td>
    </tr>
  );
}
