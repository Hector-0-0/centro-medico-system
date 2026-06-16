/**
 * Campo de búsqueda reutilizable (filtra la tabla en el cliente, igual que
 * el TablaManager.filtrarPorTexto del proyecto desktop).
 */
export default function Buscador({ value, onChange, placeholder = 'Buscar...', ancho = 300 }) {
  return (
    <div style={{ position: 'relative', width: ancho }}>
      <span style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', fontSize: 14, opacity: 0.6 }}>🔍</span>
      <input
        value={value}
        onChange={e => onChange(e.target.value)}
        placeholder={placeholder}
        style={{
          width: '100%', padding: '10px 12px 10px 34px', fontSize: 14, boxSizing: 'border-box',
          border: '1.5px solid #d1d5db', borderRadius: 8, outline: 'none', background: '#fff',
        }}
      />
    </div>
  );
}
