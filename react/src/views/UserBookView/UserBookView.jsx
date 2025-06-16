import { useEffect, useState } from "react";
import axios from "axios";
import styles from "./UserBookView.module.css";
import { NavLink } from "react-router-dom";
import { use } from "react";
import { useContext } from "react";
import { UserContext } from "../../context/UserContext";


function BookCover({ isbn, alt }) {
    const [src, setSrc] = useState("");
    const [valid, setValid] = useState(true);

    useEffect(() => {
        const url = `https://covers.openlibrary.org/b/isbn/${isbn}-L.jpg`;
        fetch(url).then(res => {
            if (res.ok && res.headers.get("content-type").startsWith("image/")) {
                setSrc(url);
            } else {
                setValid(false);
            }
        }).catch(() => setValid(false));
    }, [isbn]);

    return (
        <img
            src={valid ? src : "/img/MythicalBook.png"}
            alt={alt}
            style={{ width: "80px", height: "auto", borderRadius: "6px" }}
        />
    );
}

export default function UserBookView() {

    const [userBooks, setUserBooks] = useState([]);
    const [error, setError] = useState("");
    const { user } = useContext(UserContext);
    const [showForm, setShowForm] = useState(false);
    const [modalData, setModalData] = useState({ show: false, bookId: null, notes: "", rating: 1 });
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

    const handleUpdateUserBook = (bookId, updates) => {
        axios.put(`/users/${user.id}/books/${bookId}/update`, updates)
            .then(() => axios.get(`/users/${user.id}/books`))
            .then(resp => setUserBooks(resp.data))
            .catch(err => {
                console.error(err);
                setError("Failed to update book.");
            });
    };


    if (!user) return <p>Loading user...</p>;


    return (
        <div className={styles.container}>
            <h2 className={styles.h2}>{user.username}'s Books</h2>
            {userBooks.length > 0 ? (
                <ul className={styles.bookList}>
                    {userBooks.map((userBook) => (


                        <li key={userBook.book.bookId} className={styles.bookItem}>
                            <BookCover isbn={userBook.book.isbn} alt={`Cover for ${userBook.book.title}`} />
                            <div className={styles.bookDetails}>

                                <div className={styles.bookRow}><strong>{userBook.book.title}</strong> by {userBook.book.author}</div>

                                <div className={styles.bookRow}>📅 Start Date: {userBook.dateStarted || "N/A"}</div>
                                <div className={styles.bookRow}> ✅ Date Completed: {userBook.dateFinished || "N/A"}</div>
                                <div className={styles.bookRow}>
                                    📘 Currently Reading:
                                    <input
                                        type="checkbox"
                                        checked={userBook.currentlyReading}
                                        onChange={(e) => handleUpdateUserBook(userBook.book.bookId, {
                                            currentlyReading: e.target.checked,
                                            dateFinished: userBook.dateFinished || null,
                                            notes: userBook.notes || "",
                                            rating: userBook.rating || 0
                                        })}
                                    />
                                </div>
                                <div className={styles.bookRow}>Notes: {userBook.notes || "No notes"}</div>
                                <div className={styles.bookRow}>Rating: {userBook.rating ? `${userBook.rating} ⭐` : "No rating"}</div>
                                {userBook.dateFinished ? (
    <button className={styles.completeButton} disabled>
        Completed
    </button>
) : (
    <button
        className={styles.completeButton}
        onClick={() => {
            setModalData({
                show: true,
                bookId: userBook.book.bookId,
                notes: userBook.notes || "",
                rating: userBook.rating || 0
            });
        }}
    >
        Mark as Completed
    </button>
)}
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No books found.</p>
            )}
            <button className={styles.btnPrimary} onClick={() => setShowForm(prev => !prev)}>
                {showForm ? "Cancel" : "Add a Book"}
            </button><br /><br /><br />
            {showForm && (
                <form onSubmit={handleAddBook} className={styles.addBookForm}>
                    <div className={styles.formControl}>
                        <label className={styles.formControlLabel} required>*Title:</label>
                        <input
                            type="text"
                            className={styles.formControlInput}
                            value={newBook.title}
                            onChange={(e) => setNewBook({ ...newBook, title: e.target.value })}
                            required
                            autoFocus
                        />
                    </div>
                    <div className={styles.formControl}>
                        <label className={styles.formControlLabel} required>*Author:</label>
                        <input
                            type="text"
                            className={styles.formControlInput}
                            value={newBook.author}
                            onChange={(e) => setNewBook({ ...newBook, author: e.target.value })}
                            required
                        />
                    </div>
                    <div className={styles.formControl}>
                        <label className={styles.formControlLabel} required>*ISBN:</label>
                        <input
                            type="text"
                            className={styles.formControlInput}
                            value={newBook.isbn}
                            onChange={(e) => setNewBook({ ...newBook, isbn: e.target.value })}
                            required
                        />
                    </div>
                    <div className={styles.formControl}>
                        <label className={styles.formControlLabel} required>*Start Date:</label>
                        <input
                            type="date"
                            className={styles.formControlInput}
                            value={newBook.dateStarted}
                            onChange={(e) => setNewBook({ ...newBook, dateStarted: e.target.value })}
                        />
                    </div>

                    <div className={styles.formControl}>
                        <label className={styles.formControlLabel}>Currently Reading:</label>
                        <input
                            type="checkbox"
                            className={styles.checkbox}
                            checked={newBook.currentlyReading}
                            onChange={(e) => setNewBook({ ...newBook, currentlyReading: e.target.checked })}
                        />
                    </div>
                    <button type="submit" className={styles.btnPrimary}>Add Book</button>
                </form>
            )}
            {modalData.show && (
                <div className={styles.modalOverlay}>
                    <div className={styles.modalContent}>
                        <h3>Complete Book</h3>
                        <label>
                            Notes:
                            <textarea
                                value={modalData.notes}
                                onChange={(e) => setModalData({ ...modalData, notes: e.target.value })}
                            />
                        </label>
                        <label>
                            Rating (1-5):
                            <input
                                type="number"
                                min="1"
                                max="5"
                                value={modalData.rating}
                                onChange={(e) => setModalData({ ...modalData, rating: parseInt(e.target.value) })}
                            />
                        </label>
                        <button onClick={() => {
                            const today = new Date().toISOString().split("T")[0];
                            handleUpdateUserBook(modalData.bookId, {
                                currentlyReading: false,
                                dateFinished: today,
                                notes: modalData.notes,
                                rating: modalData.rating
                            });
                            setModalData({ show: false, bookId: null, notes: "", rating: 0 });
                        }}>Save</button>
                        <button onClick={() => setModalData({ show: false, bookId: null, notes: "", rating: 0 })}>Cancel</button>
                    </div>
                </div>
            )}

        </div>

    );
}