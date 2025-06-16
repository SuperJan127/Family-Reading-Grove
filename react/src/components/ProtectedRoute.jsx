import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { UserContext } from '../context/UserContext';

// This component checks if a user is logged in and optionally checks for a specific role.
export default function ProtectedRoute({ children, requiredRole }) {
  const { user } = useContext(UserContext);

  // Not logged in → redirect to login
  if (!user) {
    return <Navigate to="/login" />;
  }

  // If requiredRole is specified and doesn't match → redirect
  if (requiredRole && user.role !== requiredRole) {
    return <Navigate to="/" />;
  }

  // Authorized → show the child component
  return children;
}