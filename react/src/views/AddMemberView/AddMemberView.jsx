import { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import Notification from '../../components/Notification/Notification';
import { UserContext } from '../../context/UserContext';
import styles from './AddMemberView.module.css'; // Assuming you have a CSS module for styles

export default function AddMemberView() {
  const { user } = useContext(UserContext);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState('ROLE_CHILD');
  const [notification, setNotification] = useState(null);
  const navigate = useNavigate();

  // Redirect if user is not available
  useEffect(() => {
    if (!user || !user.familyId) {
      setNotification({ type: 'error', message: 'You must be logged in as a parent in a family to add members.' });
    }
  }, [user]);

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!username || !password || !confirmPassword) {
      setNotification({ type: 'error', message: 'All fields are required.' });
      return;
    }

    if (password !== confirmPassword) {
      setNotification({ type: 'error', message: 'Passwords do not match.' });
      return;
    }

    // Extra safety check
    if (!user || !user.familyId) {
      setNotification({ type: 'error', message: 'No family ID found. Please log in again.' });
      return;
    }

    const newMember = {
      username,
      password,
      confirmPassword,
      role
    };

    AuthService.addFamilyMember(user.familyId, newMember)
      .then(() => {
        setNotification({ type: 'success', message: 'Member added successfully!' });
        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setTimeout(() => navigate('/parent'), 1000);
      })
      .catch(error => {
        setNotification({ type: 'error', message: error.response?.data?.message || 'Error adding member.' });
      });
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.h2}>Add Family Member</h2>
      <Notification notification={notification} clearNotification={() => setNotification(null)} />

      <form onSubmit={handleSubmit}>
        <div className={styles.formControl}>
          <label htmlFor="username" className={styles.formControlLabel}>Username:</label>
          <input
            id="username" className={styles.formControlInput}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required autoFocus
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="password" className={styles.formControlLabel}>Password:</label>
          <input
            type="password"
            id="password" className={styles.formControlInput}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="confirmPassword" className={styles.formControlLabel}>Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword" className={styles.formControlInput}
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="role" className={styles.formControlLabel}>Role:</label>
          <select
            id="role" className={styles.formControlInput}
            value={role}
            onChange={(e) => setRole(e.target.value)}
          >
            <option value="ROLE_CHILD">Child</option>
            <option value="ROLE_PARENT">Parent</option>
          </select>
        </div>

        <button type="submit" className={styles.btnPrimary}>Add Member</button>
      </form>
    </div>
  );
}
