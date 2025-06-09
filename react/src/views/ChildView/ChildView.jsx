
import styles from './ChildView.module.css';


export default function ChildView() {
    return(
        <body className={styles.body}>
        <div className={styles.container}>
            <p>Child View</p>
        </div>
        </body>
    )
}