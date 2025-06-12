import { useEffect, useState } from "react";
import axios from "axios";
import styles from "./UserBookView.module.css";
import { NavLink } from "react-router-dom";
import { use } from "react";
import { useContext } from "react";
import { UserContext } from "../../context/UserContext";

export default function UserBookView() {

    const [userBooks, setUserBooks] = useState([]);
    const [error, setError] = useState("");
    const { user } = useContext(UserContext);
    const [showForm, setShowForm] = useState(false);
    const [newBook, setNewBook] = useState({
        title: '',
        author: '',
        isbn: '',
        dateStarted: '',
        dateFinished: '',
        currentlyReading: true
    });

    useEffect(() => {

        if (!user || !user.id) return;

        axios.get(`/users/${user.id}/books`)
            .then(resp => {
                setUserBooks(resp.data);
            })
            .catch(err => {
                console.error(err);
                setError("Failed to load user books.");
            });
    }, [user]);

    const handleAddBook = (e) => {
        e.preventDefault();

        const userBookPayload = {
            userId: user.id,
            book: {
              title: newBook.title,
              author: newBook.author,
              isbn: newBook.isbn
            },
            currentlyReading: newBook.currentlyReading,
            dateStarted: newBook.dateStarted || null,
            dateFinished: newBook.dateFinished || null
          };

        axios.post(`/users/${user.id}/books/add`, userBookPayload)
            .then(() => axios.get(`/users/${user.id}/books`))
            .then(resp => {
                setUserBooks(resp.data);
                setNewBook({ title: "", author: "", isbn: "" });
                setShowForm(false);
            })
            .catch(err => {
                console.error(err);
                setError("Failed to add book.");
            });
    };
    console.log('userBooks', userBooks);
    if (!user) return <p>Loading user...</p>;


    return (
        <div>
            <h2>{user.username}'s Books</h2>
            {userBooks.length > 0 ? (
                <ul className={styles.bookList}>
                    {userBooks.map((userBook) => (
                        <li key={userBook.book.bookId} className={styles.bookItem}>
                        <div><strong>{userBook.book.title}</strong> by {userBook.book.author}</div>
                        <div>📘 Currently Reading: {userBook.currentlyReading ? "Yes" : "No"}</div>
                        <div>📅 Start Date: {userBook.dateStarted || "N/A"}</div>
                        <div>✅ Date Finished: {userBook.dateFinished || "N/A"}</div>
                      </li>
                    ))}
                </ul>
            ) : (
                <p>No books found.</p>
            )}
            <button onClick={() => setShowForm(prev => !prev)}>
                {showForm ? "Cancel" : "Add a Book"}
            </button><br /><br /><br />
            {showForm && (
                <form onSubmit={handleAddBook} className={styles.addBookForm}>
                    <label>
                        Title:
                        <input
                            type="text"
                            value={newBook.title}
                            onChange={(e) => setNewBook({ ...newBook, title: e.target.value })}
                            required
                            autoFocus
                        />
                    </label>
                    <label>
                        Author:
                        <input
                            type="text"
                            value={newBook.author}
                            onChange={(e) => setNewBook({ ...newBook, author: e.target.value })}
                            required
                        />
                    </label>
                    <label>
                        ISBN:
                        <input
                            type="text"
                            value={newBook.isbn}
                            onChange={(e) => setNewBook({ ...newBook, isbn: e.target.value })}
                            required
                        />
                    </label>
                    <label>
                        Start Date:
                        <input
                            type="date"
                            value={newBook.dateStarted}
                            onChange={(e) => setNewBook({ ...newBook, dateStarted: e.target.value })}
                        />
                    </label>

                    <label>
                        Date Finished:
                        <input
                            type="date"
                            value={newBook.dateFinished}
                            onChange={(e) => setNewBook({ ...newBook, dateFinished: e.target.value })}
                        />
                    </label>

                    <label>
                        Currently Reading:
                        <input
                            type="checkbox"
                            checked={newBook.currentlyReading}
                            onChange={(e) => setNewBook({ ...newBook, currentlyReading: e.target.checked })}
                        />
                    </label>
                    <button type="submit">Add Book</button>
                </form>
            )}

        </div>
    );
}