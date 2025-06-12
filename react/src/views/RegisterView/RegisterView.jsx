import { useState } from 'react';

import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import Notification from '../../components/Notification/Notification';
import styles from './RegisterView.module.css';

export default function RegisterView() {
  const navigate = useNavigate();

  const [notification, setNotification] = useState(null);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const role = 'ROLE_PARENT';

  const [newFamilyName, setNewFamilyName] = useState(''); // ✅ Always required now

  function handleSubmit(event) {
    event.preventDefault();

    // ✅ Check that family name is present
    if (!newFamilyName || newFamilyName.trim() === '') {
      setNotification({ type: 'error', message: 'Please enter a family name.' });
      return;
    }

    if (password !== confirmPassword) {
      setNotification({ type: 'error', message: 'Passwords do not match.' });
      return;
    }

    // Simplified to just use newFamilyName (familyId removed)
    const registerData = {
      username,
      password,
      confirmPassword,
      role,
      newFamilyName
    };

    AuthService.register(registerData)
      .then(() => {
        setNotification({ type: 'success', message: 'Registration successful' });
        navigate('/login');
      })
      .catch((error) => {
        const message = error.response?.data?.message || 'Registration failed.';
        setNotification({ type: 'error', message });
      });
  }

  return (
    <div className={styles.container}>
      <h2 className={styles.h2}>Register</h2>

      <Notification notification={notification} clearNotification={() => setNotification(null)} />

      <form onSubmit={handleSubmit}>
        <div className={styles.formControl}>
          <label htmlFor="username" className={styles.formControlLabel}>Username:</label>
          <input
            type="text"
            id="username"
            className={styles.formControlInput}
            value={username}
            size="50"
            required
            autoFocus
            autoComplete="username" 
            onChange={(event) => setUsername(event.target.value)}
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="password" className={styles.formControlLabel}>Password:</label>
          <input
            type="password"
            id="password"
            className={styles.formControlInput}
            value={password}
            size="50"
            required
            onChange={(event) => setPassword(event.target.value)}
          />
        </div>

        <div className={styles.formControl}>
          <label htmlFor="confirmPassword" className={styles.formControlLabel}>Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            className={styles.formControlInput}
            value={confirmPassword}
            size="50"
            required
            onChange={(event) => setConfirmPassword(event.target.value)}
          />
        </div>


        <div className={styles.formControl}>
          <label htmlFor="familyName" className={styles.formControlLabel}>Family Name:</label>
          <input
            type="text"
            id="familyName"
            className={styles.formControlInput}
            value={newFamilyName}
            required
            onChange={(e) => setNewFamilyName(e.target.value)}
          />
        </div>

        <button type="submit" className={styles.btnPrimary}>
          Register
        </button>

        <div style={{ textAlign: 'center', marginTop: '1rem' }}>
          <button
            type="button"
            className={styles.btnPrimary}
            onClick={() => navigate('/login')}
          >
            Have an account? Log in
          </button>
        </div>
      </form>
    </div>
  );
}
