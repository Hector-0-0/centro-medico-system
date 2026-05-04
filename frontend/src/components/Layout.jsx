import Sidebar from './Sidebar';

const s = {
  wrapper: { display: 'flex', minHeight: '100vh' },
  main: { marginLeft: 240, flex: 1, padding: 32, background: '#f5f6fa', minHeight: '100vh' },
};

/** Layout base con sidebar fijo para todas las páginas privadas. */
export default function Layout({ children }) {
  return (
    <div style={s.wrapper}>
      <Sidebar />
      <main style={s.main}>{children}</main>
    </div>
  );
}
