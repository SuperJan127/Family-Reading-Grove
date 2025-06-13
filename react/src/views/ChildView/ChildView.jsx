import { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { NavLink } from 'react-router-dom';
import { UserContext } from '../../context/UserContext';
// Reuse the ParentView stylesheet so your classes line up 1:1
import styles from '../ParentView/ParentView.module.css';

export default function ChildView() {
  const { user } = useContext(UserContext);

  const [error, setError] = useState('');
  const [readingHistory, setReadingHistory] = useState([]);
  const [loadingHistory, setLoadingHistory] = useState(true);
  const [historyError, setHistoryError] = useState('');

  const [completedCount, setCompletedCount] = useState(null);
  const [countError, setCountError] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    const familyId = user.familyId;

    if (!familyId) {
      setError('Family ID not found.');
      setLoadingHistory(false);
      return;
    }

    const headers = { Authorization: `Bearer ${token}` };

    axios
      .get(`/families/${familyId}/reading-activities`, { headers })
      .then((resp) => {
        setReadingHistory(resp.data);
      })
      .catch((err) => {
        console.error(err);
        setHistoryError('Failed to load reading history.');
      })
      .finally(() => {
        setLoadingHistory(false);
      });
  }, [user.familyId]);

  return (
    <>
      <h2 className={styles.h2}>Family Activity</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <div className={styles.tableContainer}>
        <img
          src="src/img/FamilyActivity.png"
          alt="Family Activity"
          className={styles.image}
        />

        <table className={styles.table}>
          <thead>
            <tr>
              <th colSpan="4">Reading Tracking</th>
            </tr>
            <tr>
              <th>Reader</th>
              <th>Book Title</th>
              <th>Author</th>
              <th>Minutes Read</th>
            </tr>
          </thead>
          <tbody>
            {loadingHistory ? (
              <tr>
                <td colSpan="4">Loading Reading History...</td>
              </tr>
            ) : historyError ? (
              <tr>
                <td colSpan="4" style={{ color: 'red' }}>
                  {historyError}
                </td>
              </tr>
            ) : readingHistory.length > 0 ? (
              readingHistory.map((entry) => (
                <tr key={entry.id}>
                  <td>{entry.username}</td>
                  <td>{entry.title}</td>
                  <td>{entry.author}</td>
                  <td>{entry.minutes}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4">No reading history yet.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
      <br />
      <NavLink to="/addReading" className={styles.buttonPrimary}>
        Add Book
      </NavLink>
    </>
  );
}

//TODO : Add Book buton