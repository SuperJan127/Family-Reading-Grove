import { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { NavLink } from 'react-router-dom';
import AddMemberView from '../AddMemberView/AddMemberView';
import styles from './ParentView.module.css';
import { UserContext } from '../../context/UserContext';

import useCompletedCount from '../../hooks/useCompletedCount';


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


export default function ParentView() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const { user: currentUser } = useContext(UserContext);
  const isParent = currentUser?.role === 'ROLE_PARENT';
  const [familyMinutes, setFamilyMinutes] = useState([]);
  const [readingHistory, setReadingHistory] = useState([]);
  const [loadingHistory, setLoadingHistory] = useState(true);
  const [historyError, setHistoryError] = useState('');

  const [familyName, setFamilyName] = useState('');
  const [editingName, setEditingName] = useState(false);

  const {
    count: completedCount,
    errorMessage: countError
  } = useCompletedCount(currentUser?.id);

  function formatRole(role) {
    switch (role) {
      case 'ROLE_PARENT':
        return 'Parent';
      case 'ROLE_CHILD':
        return 'Child';
      default:
        return role; // fallback in case a new role is added
    }
  }
  // Load family reading minutes by user
  useEffect(() => {
    if (currentUser.familyId) {
      axios.get(`/reading-activities/family/${currentUser.familyId}/minutes-by-user`)
        .then(res => setFamilyMinutes(res.data))
        .catch(err => console.error("Error loading family reading minutes", err));
    }
  }, [currentUser.familyId]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const currentUser = JSON.parse(localStorage.getItem('user'));
    const familyId = currentUser?.familyId;

    if (!familyId) {
      setError('Family ID not found.');
      setLoadingHistory(false);
      return;
    }

    const headers = { Authorization: `Bearer ${token}` };
    const memberRequest = axios.get(`/families/${familyId}/members`, { headers });
    const historyRequest = axios.get(`/families/${familyId}/reading-activities`, { headers });
    const familyRequest = axios.get(`/families/${familyId}`, { headers });

    Promise.all([memberRequest, historyRequest, familyRequest])
      .then(([memberResponse, historyResponse, familyResponse]) => {
        setUsers(memberResponse.data);
        setReadingHistory(historyResponse.data);
        setFamilyName(familyResponse.data.familyName); // Assuming familyName is returned in the response
      })
      .catch((err) => {
        console.error(err);
        setError('Failed to load family data.');
        setHistoryError('Failed to load reading history.');
      })
      .finally(() => {
        setLoadingHistory(false);
      });
  }, []);


  const handleUpdateFamilyName = (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    const familyId = currentUser?.familyId;

    axios
      .put(`/families/${familyId}`, { familyName }, { headers })
      .then(() => setEditingName(false))
      .catch((err) => {
        console.error(err);
        setError('Failed to update family name.');
      });
  };
  console.log("familyMinutes:", familyMinutes);
  const totalFamilyMinutes = familyMinutes.reduce(
    (sum, user) => sum + user.totalMinutes,
    0
  );
  return (
    <>

      <h2 className={styles.h2}>
        {familyName ? `${familyName} Family Reading Activity` : 'Family Reading Activity'}
      </h2>

      {isParent && editingName && (
        <form onSubmit={handleUpdateFamilyName} className={styles.editForm}>
          <input
            type="text"
            value={familyName}
            onChange={(e) => setFamilyName(e.target.value)}
            required
            className={styles.textInput}
          />
          <button type="submit" className={styles.buttonPrimary}>Save</button>
          <button
            type="button"
            onClick={() => setEditingName(false)}
            className={styles.buttonSecondary}
          >
            Cancel
          </button>
        </form>
      )}

      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      <div className={styles.tableRow}>
        {/* ✅ New wrapper for image + members table */}
        <div className={styles.imageAndFamily}>
          <img src="src/img/FamilyActivity.png" alt="Family Activity" className={styles.image} />

          {/* 🔁 Moved family members table here */}
          <div className={styles.familyContainer}>
            <table className={styles.familyTable}>
              <thead>
                <tr><th colSpan="2">Family Members</th></tr>
                <tr><th>Username</th><th>Role</th></tr>
              </thead>
              <tbody>
                {users.length ? (
                  users.map(u => (
                    <tr key={u.id}>
                      <td>
                        {isParent ? (
                          <NavLink to={`/reader/${u.id}`} className={styles.readerLink}>
                            {u.username.charAt(0).toUpperCase() + u.username.slice(1).toLowerCase()}
                          </NavLink>
                        ) : (
                          u.username.charAt(0).toUpperCase() + u.username.slice(1).toLowerCase()
                        )}
                      </td>
                      <td>{formatRole(u.role)}</td>
                    </tr>
                  ))
                ) : (
                  <tr><td colSpan="2">No members found.</td></tr>
                )}
              </tbody>
            </table>

            <div className={styles.familyButtons}>
              {isParent && (
                <>
                  <div className={styles.singleButtonWrapper}>
                    <NavLink to="/addMember" className={styles.buttonPrimary}>Add Family Member</NavLink>
                  </div>
                  {!editingName && (
                    <div className={styles.singleButtonWrapper}>
                      <button
                        className={styles.buttonPrimary}
                        onClick={() => setEditingName(true)}
                      >
                        Edit Family Name
                      </button>
                    </div>
                  )}
                </>
              )}
            </div>
          </div>
        </div>

        <div className={styles.tableSection}>
          <div className={styles.leftColumn}>
            <table className={styles.table}>
              <thead><tr><th>Your Completed Book Count</th></tr></thead>
              <tbody>
                {countError ? (
                  <tr><td style={{ color: 'red' }}>{countError}</td></tr>
                ) : completedCount === null ? (
                  <tr><td>Loading…</td></tr>
                ) : (
                  <tr><td><strong>{completedCount}<NavLink to="/apple" className={styles.appleLink}>🍎</NavLink></strong></td></tr>
                )}
              </tbody>
            </table>
          </div>

          <div className={styles.tableSection}>
            <table className={styles.table}>
              <thead>
                <tr><th colSpan="2">Family Reading Minutes</th></tr>
                <tr><th>Username</th><th>Total Minutes</th></tr>
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
        </div>

        <div className={styles.rightColumn}>
          <div className={styles.tableSection}>
            <table className={styles.table}>
              <thead>
                <tr><th colSpan="8">Reading Tracking</th></tr>
                <tr>
                  <th>Book Cover</th><th>Reader</th><th>Book Title</th>
                  <th>Author</th><th>Reading Format</th><th>Minutes Read</th>
                  <th>Date</th><th>Notes</th>
                </tr>
              </thead>
              <tbody>
                {loadingHistory ? (
                  <tr><td colSpan="8">Loading Reading History...</td></tr>
                ) : historyError ? (
                  <tr><td colSpan="8" style={{ color: 'red' }}>{historyError}</td></tr>
                ) : readingHistory.length > 0 ? (
                  readingHistory.map((entry) => (
                    <tr key={entry.id}>
                      <td><BookCover isbn={entry.isbn} alt={`Cover for ${entry.title}`} /></td>
                      <td>{entry.username.charAt(0).toUpperCase() + entry.username.slice(1)}</td>
                      <td>{entry.title}</td>
                      <td>{entry.author}</td>
                      <td>{entry.format}</td>
                      <td>{entry.minutes}</td>
                      <td>{entry.date ? new Date(entry.date).toLocaleDateString() : "No date"}</td>
                      <td>{entry.notes}</td>
                    </tr>
                  ))
                ) : (
                  <tr><td colSpan="8">No reading history yet.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div className={styles.buttonGroup}>
        <NavLink to="/prizes" className={styles.buttonPrimary}>View Prizes</NavLink>
        <NavLink to="/userBooks" className={styles.buttonPrimary}>My Books</NavLink>
      </div>
    </>
  );
}




