import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { UserContext } from '../context/UserContext';

// export default function ProtectedRoute({ children }) {

  // Get the user from the user context
  // const user = useContext(UserContext);

  // If there's an authenticated user, continue to child route
  // if (user) {
  //   return children;
  // }

  // Otherwise, send to login page
//   return <Navigate to="/login" />;
// }  

 // OLD ↑↑↑   NEW ↓↓↓ 
// This component checks if a user is logged in and optionally checks for a specific role.
export default function ProtectedRoute({ children, requiredRole }) {
  // Destructure user from context (changed from just 'user' variable)
  const { user } = useContext(UserContext);

  if (!user) {
    // Not logged in → redirect to login
    return <Navigate to="/login" />;
  }

  // New check: if a requiredRole prop is given, check if user has that role
  if (requiredRole && user.role !== requiredRole) {
    // Logged in but user lacks the required role → redirect to homepage or another page
    return <Navigate to="/" />;
  }

  // User is logged in and has required role (if any)
  return children;
}