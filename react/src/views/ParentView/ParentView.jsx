import { useEffect, useState } from 'react';
import axios from 'axios';
import { NavLink } from 'react-router-dom';
import AddMemberView from '../AddMemberView/AddMemberView';

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
    <div>
      <h2>Family Members</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <table>
        <thead>
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
      </table><br /><br />

    <NavLink to="/addMember" className="btn btn-primary">Add Family Member</NavLink>
        
    </div>
  );
}



