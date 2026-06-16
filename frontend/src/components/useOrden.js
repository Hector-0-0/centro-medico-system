import { useState } from 'react';

/**
 * Hook de ordenamiento de tablas por columna (replica el RowSorter del
 * TablaManager del desktop: clic en la cabecera ordena asc/desc).
 *
 * Uso:
 *   const orden = useOrden();
 *   const filas = orden.ordenar(datos, { nombre: x => x.nombre, edad: x => x.edad });
 *   <ThOrden label="Nombre" col="nombre" orden={orden} style={s.th} />
 */
export function useOrden(colInicial = null) {
  const [estado, setEstado] = useState({ col: colInicial, dir: 1 });

  const toggle = (col) =>
    setEstado(e => (e.col === col ? { col, dir: -e.dir } : { col, dir: 1 }));

  const flecha = (col) => (estado.col === col ? (estado.dir === 1 ? ' ▲' : ' ▼') : '');

  const ordenar = (arr, getters) => {
    if (!estado.col || !getters[estado.col]) return arr;
    const g = getters[estado.col];
    return [...arr].sort((a, b) => {
      const va = g(a), vb = g(b);
      if (va == null && vb == null) return 0;
      if (va == null) return 1;
      if (vb == null) return -1;
      if (typeof va === 'number' && typeof vb === 'number') return (va - vb) * estado.dir;
      return String(va).localeCompare(String(vb), 'es', { numeric: true, sensitivity: 'base' }) * estado.dir;
    });
  };

  return { toggle, flecha, ordenar };
}
