import { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { NavLink } from 'react-router-dom';
import AddMemberView from '../AddMemberView/AddMemberView';
import styles from './ParentView.module.css';
import { UserContext } from '../../context/UserContext';

import useCompletedCount from '../../hooks/useCompletedCount';


export default function ParentView() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const { user: currentUser } = useContext(UserContext);
  const isParent = currentUser?.role === 'ROLE_PARENT';

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

      <div className={styles.tableContainer}>
        <img src="src/img/FamilyActivity.png" alt="Family Activity" className={styles.image} />

        <div className={styles.smallTableWrapper}>
          <table className={styles.table}>
            <thead>
              <tr><th>Books Completed</th></tr>
            </thead>
            <tbody>
              {countError ? (
                <tr><td style={{ color: 'red' }}>{countError}</td></tr>
              ) : completedCount === null ? (
                <tr><td>Loading…</td></tr>
              ) : (
                <tr><td><strong>{completedCount}</strong></td></tr>
              )}
            </tbody>
          </table>
        </div>

        <div className={styles.largeTableWrapper}>
          <table className={styles.table}>
            <thead>
              <tr><th colSpan="5">Reading Tracking</th></tr>
              <tr>
                <th>Reader</th><th>Book Title</th><th>Author</th>
                <th>Minutes Read</th><th>Notes</th>
              </tr>
            </thead>
            <tbody>
              {loadingHistory ? (
                <tr><td colSpan="5">Loading Reading History...</td></tr>
              ) : historyError ? (
                <tr><td colSpan="5" style={{ color: 'red' }}>{historyError}</td></tr>
              ) : readingHistory.length ? (
                readingHistory.map(entry => (
                  <tr key={entry.id}>
                    <td>{entry.username}</td>
                    <td>{entry.title}</td>
                    <td>{entry.author}</td>
                    <td>{entry.minutes}</td>
                    <td>{entry.notes}</td>
                  </tr>
                ))
              ) : (
                <tr><td colSpan="5">No reading history yet.</td></tr>
              )}
            </tbody>
          </table>
        </div>

        <div className={styles.memberSection}>
          <table className={styles.familyTable}>
            <thead>
              <tr><th colSpan="2">Family Members</th></tr>
              <tr><th>Username</th><th>Role</th></tr>
            </thead>
            <tbody>
              {users.length ? (
                users.map(u => (
                  <tr key={u.id}>
                    <td>{u.username}</td>
                    <td>{formatRole(u.role)}</td>
                  </tr>
                ))
              ) : (
                <tr><td colSpan="2">No members found.</td></tr>
              )}
            </tbody>
          </table>

          {isParent && (
            <>
              <div className={styles.singleButtonWrapper}>
                <NavLink to="/addMember" className={styles.buttonPrimary}>
                  Add Family Member
                </NavLink>
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

      <br /><br />

      <div className={styles.buttonGroup}>
        <NavLink to="/prizes" className={styles.buttonPrimary}>View Prizes</NavLink>
        <NavLink to="/addBook" className={styles.buttonPrimary}>Add Book</NavLink>
      </div>
    </>
  );
}




