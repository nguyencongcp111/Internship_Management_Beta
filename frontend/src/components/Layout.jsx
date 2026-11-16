import Sidebar from './Sidebar';
import TrianglePattern from './TrianglePattern';
import styles from './Layout.module.css';

export default function Layout({ children }) {
  return (
    <div className={styles.root}>
      {/* Blue gradient background */}
      <div className={styles.bgGradient} />
      {/* Triangle pattern bottom-right */}
      <TrianglePattern colorMode="blue" />

      <div className={styles.inner}>
        <Sidebar />
        <main className={styles.main}>
          <div className={styles.contentCard}>
            {children}
          </div>
        </main>
      </div>
    </div>
  );
}
