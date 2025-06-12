import { useEffect, useState } from "react";
import axios from "axios";
import styles from "./BooksListView.module.css";
import { NavLink } from "react-router-dom";

export default function BooksListView() {
  const [books, setBooks]   = useState([]);
  const [error, setError]   = useState("");

  useEffect(() => {
    axios.get("/books")
      .then(resp => {
        setBooks(resp.data);
      })
      .catch(err => {
        console.error(err);
        setError("Failed to load books.");
      });
  }, []);

  return (
    <div>
      <h2 className={styles.container}>All Books</h2>
     <div className={styles.imgdiv}>

     <img src="src/img/BookStack.png" alt="Book Stack" className={styles.image} />
     <div className={styles.tableButton}>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {books.length === 0
        ? <p>No books found.</p>
        : (
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Title</th>
                <th>Author</th>
                <th>ISBN</th>
              </tr>
            </thead>
            <tbody>
              {books.map(book => (
                <tr key={book.bookId}>
                  <td>{book.title}</td>
                  <td>{book.author}</td>
                  <td>{book.isbn}</td>
                </tr>
              ))}
            </tbody>
          </table>
          
        )
      }
      <div className={styles.buttonGroup}>
        <NavLink to="/addBook" className={styles.buttonPrimary}>Add Book</NavLink>
        <NavLink to="/userBooks" className={styles.buttonPrimary}>My Books</NavLink>
      </div>
    </div>
    </div>
    </div>
  );
}
