import { useCallback, useEffect, useRef, useState } from 'react';
import { mensajeError } from '../services/api';

/**
 * Hook para cargar datos asíncronos con estados de carga/error unificados.
 *
 * Resuelve dos problemas que se repetían en todas las páginas de lista:
 *  - los `catch {}` silenciosos (un fallo de red mostraba "Sin datos", idéntico
 *    a una lista vacía): aquí se expone `error` con un mensaje legible.
 *  - el parpadeo "Sin datos" antes de la primera respuesta: se expone `cargando`.
 *
 * Además evita el "setState tras desmontar" usando una bandera de montado.
 *
 *   const { datos, cargando, error, recargar } = useCargar(listarCitas);
 *
 * @param loader  función async que devuelve los datos.
 * @param deps    dependencias que, al cambiar, vuelven a disparar la carga.
 */
export function useCargar(loader, deps = []) {
  const [datos, setDatos] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const montado = useRef(true);

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const cargar = useCallback(loader, deps);

  const recargar = useCallback(async () => {
    setCargando(true);
    setError('');
    try {
      const r = await cargar();
      if (montado.current) setDatos(r);
      return r;
    } catch (err) {
      if (montado.current) {
        setError(mensajeError(err, 'No se pudieron cargar los datos.'));
        setDatos(null);
      }
      return null;
    } finally {
      if (montado.current) setCargando(false);
    }
  }, [cargar]);

  useEffect(() => {
    montado.current = true;
    recargar();
    return () => {
      montado.current = false;
    };
  }, [recargar]);

  return { datos, cargando, error, recargar, setDatos };
}
