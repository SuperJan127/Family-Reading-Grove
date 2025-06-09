
import styles from './ChildView.module.css';



export default function ChildView() {
    return(
        <>
        <div className={styles.container}>
            <p>Child View</p>
        </div><br />
        <div className={styles.imgdiv}>
        <img src="src/img/BookNook3.png" alt="Book Nook" className={styles.image} />
        </div>
        </>
        
    )
}