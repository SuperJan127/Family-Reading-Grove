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

    useEffect(() => {
        const token = localStorage.getItem('token');
        const headers = { Authorization: `Bearer ${token}` };

        axios
            .get(`/users/${user.id}/books/completed-count`, { headers })
            .then((resp) => {
                setCompletedCount(resp.data.count);
            })
            .catch((err) => {
                console.error(err);
                setCountError('Could not load completed‐books count.');
            });
    }, [user.id]);

    return (
        <>
            <h2 className={styles.h2}>Family Activity</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            {/* Completed books count */}
            {countError && <p style={{ color: 'red' }}>{countError}</p>}
            {completedCount !== null && (
                <p>
                    You have completed{' '}
                    <strong>{completedCount}</strong>{' '}
                    {completedCount === 1 ? 'book' : 'books'}.
                </p>
            )}

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
            <div className={styles.buttonGroup}>
                <NavLink to="/addReading" className={styles.buttonPrimary}>
                    Add Book
                </NavLink>

                <NavLink to="/addPrize" className={styles.buttonPrimary}>
                    View Prizes
                </NavLink>
            </div>
        </>
    );
}

//TODO : Add Book buton