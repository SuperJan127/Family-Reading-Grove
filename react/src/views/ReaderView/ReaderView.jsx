// src/views/ReaderView/ReaderView.jsx
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import styles from "../ChildView/ChildView.module.css"; 

export default function ReaderView() {
  const { readerId } = useParams();
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

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

  if (loading) return <p>Loading…</p>;
  if (error)   return <p style={{ color: "red" }}>{error}</p>;

  const name = activities[0]?.username 
    ? activities[0].username.charAt(0).toUpperCase() + activities[0].username.slice(1)
    : `User #${readerId}`;

  return (
    <>
      <h2 className={styles.h2}>{name}’s Reading Activity</h2>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>Cover</th><th>Title</th><th>Author</th>
            <th>Minutes</th><th>Date</th><th>Notes</th>
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
            <tr><td colSpan="6">No reading activity yet.</td></tr>
          )}
        </tbody>
      </table>
    </>
  );
}
