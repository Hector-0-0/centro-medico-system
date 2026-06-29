import { Component } from 'react';

/**
 * Captura errores de render de las páginas para que un fallo en un módulo no
 * deje la aplicación en blanco. Se monta dentro del marco (sidebar + topbar),
 * así que el usuario sigue pudiendo navegar a otra sección.
 *
 * Se le pasa una `key` (el pathname) desde el Layout para que al cambiar de
 * ruta el límite se reinicie automáticamente.
 */
export default class ErrorBoundary extends Component {
  state = { error: null };

  static getDerivedStateFromError(error) {
    return { error };
  }

  componentDidCatch(error, info) {
    console.error('Error en una vista:', error, info);
  }

  render() {
    if (this.state.error) {
      return (
        <div>
          <h1 className="panel__title">Algo salió mal</h1>
          <div className="card-section">
            <p className="panel__hint" style={{ marginBottom: 12 }}>
              Ocurrió un error al mostrar esta sección. Puedes reintentar o ir a otra parte del menú.
            </p>
            <button
              className="btn btn--primary"
              onClick={() => this.setState({ error: null })}
            >
              Reintentar
            </button>
          </div>
        </div>
      );
    }
    return this.props.children;
  }
}
