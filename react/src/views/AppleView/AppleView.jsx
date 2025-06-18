import styles from "./AppleView.module.css";

export default function AppleView() {
  const appleCount = 896; // change this for more or fewer apples
  const apples = Array.from({ length: appleCount }, (_, i) => (
    <span key={i} className={styles.apple}>🍎</span>
  ));

  return (
    <div className={styles.container}>
      <h2 className={styles.header}>🍏 Welcome to the Orchard 🍏</h2>
      <div className={styles.grid}>
        {apples}
      </div>
    </div>
  );
}