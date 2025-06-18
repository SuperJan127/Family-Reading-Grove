// src/views/ReaderView/ReaderView.jsx
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import styles from "../ReaderView/ReaderView.module.css";
import useCompletedCount from "../../hooks/useCompletedCount";

export default function ReaderView() {
  const { readerId } = useParams();

  //  Books Completed count hook
  const { count: completedCount, errorMessage: completedError } =
    useCompletedCount(readerId);

  //  Reader’s display name
  const [readerName, setReaderName] = useState("");

  //  This reader’s session data
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  //  Fetch the reader’s activities
  useEffect(() => {
    setLoading(true);
    axios
      .get(`/reading-activities/reader/${readerId}`)
      .then(res => {
        setActivities(res.data);
        setError("");
      })
      .catch(() => setError("Unable to load that user’s activity."))
      .finally(() => setLoading(false));
  }, [readerId]);

  //  Fetch the reader’s username (so we show it even if no sessions)
  useEffect(() => {
    if (!readerId) {
      setReaderName(`User #${readerId}`);
      return;
    }
    axios
      .get(`/users/${readerId}`)
      .then(res => {
        const name = res.data.username;
        setReaderName(
          name.charAt(0).toUpperCase() + name.slice(1).toLowerCase()
        );
      })
      .catch(() => {
        setReaderName(`User #${readerId}`);
      });
  }, [readerId]);

  if (loading) return <p>Loading…</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  //  Compute total minutes read
  const totalMinutes = activities.reduce((sum, a) => sum + a.minutes, 0);

  return (
    <>
      {/* —— Header —— */}
      <h2 className={styles.h2}>{readerName}’s Reading Activity</h2>

      {/* —— Summary Tables —— */}
      <div className={styles.tableWrapper}>
        {/* Books Completed */}
        <table className={styles.table} style={{ width: 150 }}>
          <thead>
            <tr><th>Books Completed</th></tr>
          </thead>
          <tbody>
            <tr>
              <td style={{ textAlign: "center" }}>
                {completedError
                  ? "—"
                  : completedCount === null
                    ? "…"
                    : completedCount}
              </td>
            </tr>
          </tbody>
        </table>

        {/* Total Minutes */}
        <table className={styles.table} style={{ width: 150 }}>
          <thead>
            <tr><th>Total Minutes</th></tr>
          </thead>
          <tbody>
            <tr>
              <td style={{ textAlign: "center" }}>{totalMinutes}</td>
            </tr>
          </tbody>
        </table>


        {/* —— Detailed Activity Log —— */}
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Cover</th>
              <th>Title</th>
              <th>Author</th>
              <th>Minutes</th>
              <th>Date</th>
              <th>Notes</th>
            </tr>
          </thead>
          <tbody>
            {activities.map(a => (
              <tr key={a.id}>
                <td>
                  <img
                    src={`https://covers.openlibrary.org/b/isbn/${a.isbn}-M.jpg`}
                    alt={a.title}
                    style={{ width: 60 }}
                  />
                </td>
                <td>{a.title}</td>
                <td>{a.author}</td>
                <td>{a.minutes}</td>
                <td>{new Date(a.date).toLocaleDateString()}</td>
                <td>{a.notes}</td>
              </tr>
            ))}
            {activities.length === 0 && (
              <tr>
                <td colSpan="6">No reading activity yet.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </>
  );
}


