// src/views/AddReadingActivityView/AddReadingActivityView.jsx
import { useContext, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext";

export default function AddReadingActivityView() {
  const { user } = useContext(UserContext);
  const navigate = useNavigate();

  const [bookId, setBookId] = useState("");
  const [format, setFormat] = useState("PAPER");
  const [minutes, setMinutes] = useState("");
  const [notes, setNotes] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await axios.post("/reading-activities", {
        readerId: user.id,
        bookId: parseInt(bookId, 10),
        format,
        minutes: parseInt(minutes, 10),
        notes: notes || null
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
    <div>
      <h2>Record Reading Activity</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="bookId">Book ID</label><br />
          <input
            id="bookId"
            type="number"
            value={bookId}
            onChange={e => setBookId(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="format">Format</label><br />
          <select
            id="format"
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

        <div>
          <label htmlFor="minutes">Minutes Spent</label><br />
          <input
            id="minutes"
            type="number"
            min="0"
            value={minutes}
            onChange={e => setMinutes(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="notes">Notes (optional)</label><br />
          <textarea
            id="notes"
            rows="3"
            value={notes}
            onChange={e => setNotes(e.target.value)}
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Recording…" : "Record Activity"}
        </button>
      </form>
    </div>
  );
}
