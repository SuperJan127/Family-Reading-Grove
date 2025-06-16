// src/views/AddReadingActivityView/AddReadingActivityView.jsx
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext";
import styles from "./AddReadingActivityView.module.css";

export default function AddReadingActivityView() {
  const { user } = useContext(UserContext);
  const navigate = useNavigate();

  const [bookId, setBookId] = useState("");
  const [format, setFormat] = useState("PAPER");
  const [minutes, setMinutes] = useState("");
  const [notes, setNotes] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [familyMembers, setFamilyMembers] = useState([]);
  const [selectedReaderId, setSelectedReaderId] = useState("");
  const [books, setBooks] = useState([]); // Assuming you might want to fetch books later
  const [date, setDate] = useState(""); // ISO format expected: 'YYYY-MM-DD'

  useEffect(() => {
    axios
      .get(`/users/${user.id}/books`)
      .then((r) =>  setBooks(r.data))
      .catch((e) =>
        console.error("Failed to load this user's books", e)
      );
  }, [user.id]);

  useEffect(() => {
    if (user.role === "ROLE_PARENT") {
      axios
        .get(`/families/${user.familyId}/members`)
        .then((r) => {
          console.log("Family users:", r.data);
          setFamilyMembers(r.data); 
        })
        .catch((e) =>
          console.error("Failed to load family users", e)
        );
    }
  }, [user.familyId, user.role]);


  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await axios.post("/reading-activities", {
        readerId: user.role === "ROLE_PARENT" ? selectedReaderId : user.id,
        bookId: parseInt(bookId, 10),
        format,
        minutes: parseInt(minutes, 10),
        notes: notes || null,
        date: date || null // This should be in 'YYYY-MM-DD' format
      });

      // Redirect exactly like AddBookView
      if (user.role === "ROLE_PARENT") {
        navigate("/parent");
      } else if (user.role === "ROLE_CHILD") {
        navigate("/child");
      } else {
        navigate("/");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Failed to record activity. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.h2}>Record Reading Activity</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <form onSubmit={handleSubmit}>
        {user.role === "ROLE_PARENT" && (
          <div className={styles.formControl}>
          <label htmlFor="reader" className={styles.formControlLabel}>Reader:</label>
          <select
            id="reader"
            className={styles.formControlInput}
            value={selectedReaderId}
            onChange={e => setSelectedReaderId(e.target.value)}
            required
          >
            <option value="" disabled>— Select a reader —</option>
            {familyMembers.map(member => (
              member.id && (
                <option key={member.id} value={member.id}>
                  {member.username.charAt(0).toUpperCase() + member.username.slice(1)}
                  {member.role === "ROLE_PARENT" ? " (Parent)" : " (Child)"}
                </option>
              )
            ))}
          </select>
        </div>
      )}


        <div className={styles.formControl}>
          <label htmlFor="book" className={styles.formControlLabel}>
            Book:
          </label>
          <select
            id="book"
            className={styles.formControlInput}
            value={bookId}
            onChange={e => setBookId(e.target.value)}
            required
          >
            <option value="" disabled>
              — Select a title —
            </option>
            {books.map(book => (
              <option key={book.bookId} value={book.bookId}>
                {book.book.title}
              </option>
            ))}
          </select>
        </div>


        <div className={styles.formControl}>
          <label htmlFor="format" className={styles.formControlLabel}>Format:</label>
          <select
            id="format"
            className={styles.formControlInput}
            value={format}
            onChange={e => setFormat(e.target.value)}
            required
          >
            <option value="PAPER">Paper</option>
            <option value="DIGITAL">Digital</option>
            <option value="AUDIOBOOK">Audiobook</option>
            <option value="READ_ALOUD_READER">Read-Aloud (Reader)</option>
            <option value="READ_ALOUD_LISTENER">Read-Aloud (Listener)</option>
            <option value="OTHER">Other</option>
          </select>
        </div>

        <div className={styles.formControl}>
          <label htmlFor="minutes" className={styles.formControlLabel}>Minutes Spent:</label>
          <input
            id="minutes"
            type="number"
            min="0"
            className={styles.formControlInput}
            value={minutes}
            onChange={e => setMinutes(e.target.value)}
            required
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="notes" className={styles.formControlLabel}>Notes: (optional)</label>
          <textarea
            id="notes"
            rows="3"
            className={styles.formControlInput}
            value={notes}
            onChange={e => setNotes(e.target.value)}
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="date" className={styles.formControlLabel}>Date:</label>
          <input
            id="date"
            type="date"
            className={styles.formControlInput}
            value={date}
            onChange={e => setDate(e.target.value)}
            required // Optional: only if you want to make date mandatory
          />
        </div>

        <button
          type="submit"
          className={styles.btnPrimary}
          disabled={loading}
        >
          {loading ? "Recording…" : "Record Activity"}
        </button>

      </form>
    </div>
  );
}
