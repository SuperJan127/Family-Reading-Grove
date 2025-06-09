import { useState } from 'react'; // 🔄 Removed useEffect

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
  const role ='ROLE_PARENT';

  // 🔄 REPLACED: Removed useExistingFamily + existingFamilyId + families
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
    <div id="view-register">
      <h2>Register</h2>

      <Notification notification={notification} clearNotification={() => setNotification(null)} />

      <form onSubmit={handleSubmit}>
        <div className="form-control">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            size="50"
            required
            autoFocus
            autoComplete="username"
            onChange={(event) => setUsername(event.target.value)}
          />
        </div>

        <div className="form-control">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            size="50"
            required
            onChange={(event) => setPassword(event.target.value)}
          />
        </div>

        <div className="form-control">
          <label htmlFor="confirmPassword">Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            value={confirmPassword}
            size="50"
            required
            onChange={(event) => setConfirmPassword(event.target.value)}
          />
        </div>

        {/* ✅ Role is now fixed to ROLE_PARENT */} 

        {/* ✅ Family name is always shown */}
        <div className="form-control">
          <label htmlFor="familyName">Family Name:</label>
          <input
            type="text"
            id="familyName"
            value={newFamilyName}
            required
            onChange={(e) => setNewFamilyName(e.target.value)}
          />
        </div>

        <button type="submit" className={`btn-primary ${styles.formButton}`}>
          Register
        </button>
        <Link to="/login">Have an account? Log-in</Link>
      </form>
    </div>
  );
}
