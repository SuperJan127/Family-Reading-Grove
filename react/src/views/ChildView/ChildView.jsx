
import styles from './ChildView.module.css';
import { NavLink } from 'react-router-dom';


export default function ChildView() {
    return (
        <>
            <div className={styles.container}>
                <p>Child View</p>
            </div><br />
            <div className={styles.imgdiv}>
                <img src="src/img/BookNook4.png" alt="Book Nook" className={styles.image} />
                <div className={styles.buttonContainer}>
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th colSpan="3">Reading Tracking</th>
                        </tr>
                        <tr>
                            <th>Book Title</th>
                            <th>Author</th>
                            <th>Minutes Read</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>The Great Gatsby</td>
                            <td>F. Scott Fitzgerald</td>
                            <td>30</td>
                        </tr>
                        <tr>
                            <td>To Kill a Mockingbird</td>
                            <td>Harper Lee</td>
                            <td>45</td>
                        </tr>
                        <tr>
                            <td>1984</td>
                            <td>George Orwell</td>
                            <td>60</td>
                        </tr>
                        <tr>
                            <td>Pride and Prejudice</td>
                            <td>Jane Austen</td>
                            <td>25</td>
                        </tr>
                    </tbody>
                </table>
                
            <NavLink to="/addBook" className={styles.buttonPrimary}>Add Book</NavLink>
            </div>

            </div>

           
        </>
    )
}
//TODO : Add Book buton