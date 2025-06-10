import { useEffect, useState } from 'react';
import axios from 'axios';
import { NavLink } from 'react-router-dom';
import AddMemberView from '../AddMemberView/AddMemberView';
import styles from './ParentView.module.css';

export default function ParentView() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');

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
      return;
    }

    axios
      .get(`/families/${familyId}/members`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      .then((response) => {
        setUsers(response.data);
      })
      .catch((err) => {
        console.error(err);
        if (err.response?.status === 404) {
          setError('No members found for your family.');
        } else {
          setError('Failed to load family members.');
        }
      });
  }, []);

  return (
    <>
      <h2 className={styles.h2}>Family Activity</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <div className={styles.tableContainer}>
      <img src="src/img/FamilyActivity.png" alt="Family Actvity" className={styles.image} />
      
      
      <table className={styles.table}>
                    <thead>
                        <tr>
                            <th colSpan="3">Reading Tracking</th>
                        </tr>
                        <tr>
                            <th>Book Title</th>
                            <th>Author</th>
                            <th>Minutes Read</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>The Great Gatsby</td>
                            <td>F. Scott Fitzgerald</td>
                            <td>30</td>
                        </tr>
                        <tr>
                            <td>To Kill a Mockingbird</td>
                            <td>Harper Lee</td>
                            <td>45</td>
                        </tr>
                        <tr>
                            <td>1984</td>
                            <td>George Orwell</td>
                            <td>60</td>
                        </tr>
                        <tr>
                            <td>Pride and Prejudice</td>
                            <td>Jane Austen</td>
                            <td>25</td>
                        </tr>
                    </tbody>
                </table>
                <table className={styles.table}>
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
                </div><br /><br />

      <div className={styles.buttonGroup}>
        <NavLink to="/addMember" className={styles.buttonPrimary}>Add Family Member</NavLink>
      
           
    
        <NavLink to="/addBook" className={styles.buttonPrimary}>Add Book</NavLink>
      </div>

    </>
  );
}



