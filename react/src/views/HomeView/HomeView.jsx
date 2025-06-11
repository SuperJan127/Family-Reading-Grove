import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { UserContext } from '../../context/UserContext';
import styles from './HomeView.module.css'; // Assuming you have a CSS module for styles
export default function HomeView() {
  const user = useContext(UserContext);

  return (
    <div className={styles.container}>
       <div className={styles.textSection}>
      <h1 className={styles.h1}>Welcome to the Family Reading Grove!</h1>
      <br />
      <br />
      <p className={styles.p}>
        Tucked beneath curling vines and towering storybook trees, this is your family’s enchanted corner for growing lifelong reading habits—together. Whether you're a young explorer discovering picture books or a parent savoring a quiet chapter, every minute spent reading helps your family's story tree grow strong.</p>
      <br />
      <p className={styles.p}>
        Our app makes it easy (and fun!) to track reading time and log the books each family member enjoys. Simply enter the title and details of each book you read—whether it’s a bedtime favorite, a weekend chapter book, or a graphic novel adventure. As you read, your progress turns into milestones, and milestones can blossom into prizes—set and awarded by the grown-ups in your grove.
      </p>
      <br />
      <p className={styles.p}>
      Parents can create personalized reading goals, manage rewards, and encourage each reader in the family to branch out and explore the joy of books. So find a cozy spot beneath the vines, open your next book, and let your family’s reading journey grow one page at a time.
      </p>
    </div>
    <div className={styles.imageSection}>
    <img src="src/img/FamilyGrove.png" alt="Family Grove" className={styles.image} />
  </div>
    </div>
  );
}