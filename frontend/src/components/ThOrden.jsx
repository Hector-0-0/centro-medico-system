/** Cabecera de tabla ordenable (clic para ordenar asc/desc). */
export default function ThOrden({ label, col, orden, style }) {
  return (
    <th
      style={{ ...style, cursor: 'pointer', userSelect: 'none', whiteSpace: 'nowrap' }}
      onClick={() => orden.toggle(col)}
      title="Ordenar"
    >
      {label}{orden.flecha(col)}
    </th>
  );
}
