import { useContext, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext";
import styles from "./AddBookView.module.css"; // Assuming you have a CSS module for styles

export default function AddBookView() {
    const [title, setTitle] = useState("");
    const [author, setAuthor] = useState("");
    const [isbn, setIsbn] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false); // Optional loading state
    const navigate = useNavigate();
    const { user } = useContext(UserContext); // Assuming you have a UserContext to get the current user

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(""); // Reset error message
        setLoading(true); // Optionally, you can add a loading state
        try {
            await axios.post("/books", { title, author, isbn });
            navigate("/books"); // Redirect to the books list after successful addition

            // if (user?.role === "ROLE_PARENT") {
            //     navigate("/parent");
            // } else if (user?.role === "ROLE_CHILD") {
            //     navigate("/child");
            // } else {
            //     // fallback
            //     navigate("/");
            // }

        } catch (err) {
            setError(err.response?.data?.message || "Failed to add book. Please try again.");
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className={styles.container}>
          <div className={styles.contentWrapper}>
            <div className={styles.formSection}>
              <h2 className={styles.heading}>Add a New Book</h2>
      
              {error && <p className={styles.error}>{error}</p>}
      
              <form onSubmit={handleSubmit} className={styles.form}>
                <div className={styles.formGroup}>
                  <label htmlFor="title">Title</label>
                  <input id="title" type="text" value={title} onChange={e => setTitle(e.target.value)} required autoFocus />
                </div>
      
                <div className={styles.formGroup}>
                  <label htmlFor="author">Author</label>
                  <input id="author" type="text" value={author} onChange={e => setAuthor(e.target.value)} required />
                </div>
      
                <div className={styles.formGroup}>
                  <label htmlFor="isbn">ISBN</label>
                  <input id="isbn" type="text" value={isbn} onChange={e => setIsbn(e.target.value)} required />
                </div>
      
                <button type="submit" className={styles.buttonPrimary} disabled={loading}>
                  {loading ? "Adding..." : "Add Book"}
                </button>
              </form>
            </div>
      
            <div className={styles.imageSection}>
              <img src="src/img/MythicalBook.png" alt="Mythical Book" className={styles.image} />
            </div>
          </div>
        </div>
      );
}  
