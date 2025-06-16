import { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { NavLink } from 'react-router-dom';
import { UserContext } from '../../context/UserContext';
// Use the same styles as ParentView for consistent look
import styles from '../ParentView/ParentView.module.css';
// Import your custom hook
import useCompletedCount from '../../hooks/useCompletedCount';

export default function ChildView() {
    const { user } = useContext(UserContext);

    // Family reading history state
    const [familyMinutes, setFamilyMinutes] = useState([]);
    const [error, setError] = useState('');
    const [readingHistory, setReadingHistory] = useState([]);
    const [loadingHistory, setLoadingHistory] = useState(true);
    const [historyError, setHistoryError] = useState('');
    const [familyName, setFamilyName] = useState('');


    // Completed‐books count from our hook
    const { count: completedCount, errorMessage: countError } =
        useCompletedCount(user.id);

    // Gets Book Cover from Open Library, returns default image if none found
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
    // Load family reading minutes by user
    useEffect(() => {
        if (user.familyId) {
            axios.get(`/reading-activities/family/${user.familyId}/minutes-by-user`)
                .then(res => setFamilyMinutes(res.data))
                .catch(err => console.error("Error loading family reading minutes", err));
        }
    }, [user.familyId]);
    const totalFamilyMinutes = familyMinutes.reduce(
        (sum, user) => sum + user.totalMinutes,
        0
    );

    // Fetch family reading history
    useEffect(() => {
        const token = localStorage.getItem('token');
        const familyId = user.familyId;

        if (!familyId) {
            setError('Family ID not found.');
            setLoadingHistory(false);
            return;
        }

        const headers = { Authorization: `Bearer ${token}` };

        const fetchReadingHistory = axios.get(`/families/${familyId}/reading-activities`, { headers });
        const fetchFamilyInfo = axios.get(`/families/${familyId}`, { headers });


        Promise.all([fetchReadingHistory, fetchFamilyInfo])
            .then(([historyResp, familyResp]) => {
                console.log(historyResp.data);

                setReadingHistory(historyResp.data);
                setFamilyName(familyResp.data.familyName);

            })
            .catch(() => {
                setHistoryError('Failed to load reading history.');
            })
            .finally(() => {
                setLoadingHistory(false);
            });
    }, [user.familyId]);

    return (
        <>
            <h2 className={styles.h2}>
                {familyName ? `${familyName} Family Reading Activity` : 'Family Reading Activity'}
            </h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}




            <div className={styles.tableRow}>
                <img
                    src="src/img/FamilyActivity.png"
                    alt="Family Activity"
                    className={styles.image}
                />
                <div className={styles.leftColumn}>
                    {/* Books Completed Table */}
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th>Books Completed</th>
                            </tr>
                        </thead>
                        <tbody>
                            {countError ? (
                                <tr>
                                    <td style={{ color: 'red' }}>{countError}</td>
                                </tr>
                            ) : completedCount === null ? (
                                <tr>
                                    <td>Loading…</td>
                                </tr>
                            ) : (
                                <tr>
                                    <td>
                                        <strong>{completedCount}</strong>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>


                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th colSpan="2">Family Reading Minutes</th>
                            </tr>
                            <tr>
                                <th>Username</th>
                                <th>Total Minutes</th>
                            </tr>
                        </thead>
                        <tbody>
                            {familyMinutes.map(user => (
                                <tr key={user.id}>
                                    <td>{user.username.charAt(0).toUpperCase() + user.username.slice(1)}</td>
                                    <td>{user.totalMinutes}</td>
                                </tr>
                            ))}
                        </tbody>
                        <tfoot>
                            <tr>
                                <td><strong>Family Total</strong></td>
                                <td><strong>{totalFamilyMinutes}</strong></td>
                            </tr>
                        </tfoot>
                    </table>

                </div>
                <div className={styles.rightColumn}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th colSpan="7">Reading Tracking</th>
                            </tr>
                            <tr>
                                <th>Book Cover</th>
                                <th>Reader</th>
                                <th>Book Title</th>
                                <th>Author</th>
                                <th>Minutes Read</th>
                                <th>Date</th>
                                <th>Notes</th>
                            </tr>
                        </thead>
                        <tbody>
                            {loadingHistory ? (
                                <tr>
                                    <td colSpan="5">Loading Reading History...</td>
                                </tr>
                            ) : historyError ? (
                                <tr>
                                    <td colSpan="5" style={{ color: 'red' }}>
                                        {historyError}
                                    </td>
                                </tr>
                            ) : readingHistory.length > 0 ? (
                                readingHistory.map((entry) => (
                                    <tr key={entry.id}>
                                        <td><BookCover isbn={entry.isbn} alt={`Cover for ${entry.title}`} /></td>
                                        <td>{entry.username.charAt(0).toUpperCase() + entry.username.slice(1).toLowerCase()}</td>  {/* reader’s username */}
                                        <td>{entry.title}</td>
                                        <td>{entry.author}</td>
                                        <td>{entry.minutes}</td>
                                        <td>
                                            {entry.date ? new Date(entry.date).toLocaleDateString() : "No date"}
                                        </td>
                                        <td>{entry.notes}</td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="5">No reading history yet.</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
            <br />
            <div className={styles.cButtonGroup}>
                <NavLink to="/addReading" className={styles.buttonPrimary}>
                    Add Reading Activity
                </NavLink>
                <NavLink to="/userBooks" className={styles.buttonPrimary}>
                    View My Books
                </NavLink>

                <NavLink to="/prizes" className={styles.buttonPrimary}>
                    View Prizes
                </NavLink>
            </div>
        </>
    );
}

