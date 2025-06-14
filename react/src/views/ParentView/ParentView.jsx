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

    Promise.all([memberRequest, historyRequest])
      .then(([memberResponse, historyResponse]) => {
        setUsers(memberResponse.data);
        setReadingHistory(historyResponse.data);
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
        style={{  width: "80px", height: "auto", borderRadius: "6px" }}
      />
    );
  }

  return (
    <>
      <h2 className={styles.h2}>Family Activity</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <div className={styles.tableContainer}>
        <img src="src/img/FamilyActivity.png" alt="Family Actvity" className={styles.image} />

        {/* Books Completed summary table */}
        <div className={styles.smallTableWrapper}>
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
        </div>

        <div className={styles.largeTableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th colSpan="6">Reading Tracking</th>
            </tr>
            <tr>
              <th>Book Cover</th>
              <th>Reader</th>
              <th>Book Title</th>
              <th>Author</th>
              <th>Minutes Read</th>
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
                <td colSpan="5" style={{ color: 'red' }}>{historyError}</td>
              </tr>
            ) : readingHistory.length > 0 ? (
              readingHistory.map((entry) => (
                <tr key={entry.id}>
                  <td><BookCover isbn={entry.isbn} alt={`Cover for ${entry.title}`} /></td>
                  <td>{entry.username}</td>   {/* reader’s username */}
                  <td>{entry.title}</td>
                  <td>{entry.author}</td>
                  <td>{entry.minutes}</td>
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


        <div className={styles.memberSection}>
  <table className={styles.familyTable}>
    <thead>
      <tr>
        <th colSpan="2">Family Members</th>
      </tr>
      <tr>
        <th>Username</th>
        <th>Role</th>
      </tr>
    </thead>
    <tbody>
      {users.length > 0 ? (
        users.map((user) => (
          <tr key={user.id}>
            <td>{user.username}</td>
            <td>{formatRole(user.role)}</td>
          </tr>
        ))
      ) : (
        <tr>
          <td colSpan="2">No members found.</td>
        </tr>
      )}
    </tbody>
  </table>

  {isParent && (
    <div className={styles.singleButtonWrapper}>
      <NavLink to="/addMember" className={styles.buttonPrimary}>Add Family Member</NavLink>
    </div>
  )}
</div>

      </div> {/* end of .tableContainer */}
      <br /><br />

      <div className={styles.buttonGroup}>
        <NavLink to="/prizes" className={styles.buttonPrimary}>View Prizes</NavLink>
        <NavLink to="/addBook" className={styles.buttonPrimary}>Add Book</NavLink>
      </div>

    </>
  );
}



