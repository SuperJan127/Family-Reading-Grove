import { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { UserContext } from './context/UserContext';
import AuthService from './services/AuthService';
import HomeView from './views/HomeView/HomeView';
import LoginView from './views/LoginView/LoginView';
import LogoutView from './views/LogoutView';
import RegisterView from './views/RegisterView/RegisterView';
import UserProfileView from './views/UserProfileView/UserProfileView';
import ParentView from './views/ParentView/ParentView';
import ChildView from './views/ChildView/ChildView';
import AddMemberView from './views/AddMemberView/AddMemberView';
import MainNav from './components/MainNav/MainNav';
import ProtectedRoute from './components/ProtectedRoute';
import axios from 'axios';
import AddBookView from './views/AddBookView/AddBookView';
import BooksListView from './views/BooksListView/BooksListView';
import AddReadingActivityView from './views/AddReadingActivityView/AddReadingActivityView';
import styles from './App.module.css';
import UserBookView from './views/UserBookView/UserBookView';
import PrizeView from './views/PrizeView/PrizeView';
import ReaderView from './views/ReaderView/ReaderView';
import AwardedPrizeView from './views/AwardedPrizeView/AwardedPrizeView';
import AppleView from './views/AppleView/AppleView';


export default function App() {
  const [user, setUser] = useState(() => getTokenFromStorage());

  function handleLogin(userData) {
    setUser(userData);
  }

  function handleLogout() {
    // Remove auth data from local storage
    localStorage.removeItem('user');
    localStorage.removeItem('token');

    // Clear auth token from axios
    delete axios.defaults.headers.common['Authorization'];

    // Clear the auth context
    setUser(null);
  }

  // When a user comes back to the app or refreshes the page, check for user/token in local storage and validate it
  function getTokenFromStorage() {
    const user = JSON.parse(localStorage.getItem('user'));
    const token = localStorage.getItem('token');

    if (user && token) {
      // Set the token in the axios default headers
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      // Make asynchronous API request to ensure token is still valid
      AuthService.getUserProfile(user.id)
        .then(() => {
          // Token is still valid, do nothing because user is already set to state
        })
        .catch(() => {
          // Token is not valid, act like user just logged out
          handleLogout();
        });

      // Return the user object, even if it's not validated yet
      return user;
    }
    // no user/token in local storage, return null
    return null;
  }

  return (
    <BrowserRouter>
      <div id="app">
        <UserContext.Provider value={{ user }}>
          <header id="app-header" className={styles.header}>
            <div id="app-info">
              <img src="/img/FamilyReadingGroveLogo3.png" className={styles.logo} /><h1>Family Reading Grove</h1>
            </div>
            <MainNav />
          </header>
          <main id="main-content">
            <Routes>
              <Route path="/" element={<HomeView />} />
              <Route path="/login" element={<LoginView onLogin={handleLogin} />} />
              <Route path="/logout" element={<LogoutView onLogout={handleLogout} />} />
              <Route path="/register" element={<RegisterView />} />
              <Route path="/parent" element={<ParentView />} />
              <Route path="/child" element={<ChildView />} />

              <Route path="/addBook" element={<AddBookView />} />
              <Route path="/books" element={<BooksListView />} />
              <Route path="/addReading" element={<AddReadingActivityView />} />
              <Route path="/userBooks" element={<UserBookView />} />
              <Route path="/prizes" element={<PrizeView />} />
              <Route path="/prizes/awarded" element={<AwardedPrizeView />} />
              <Route path="/apple" element={<AppleView />} />


              {/* Protected routes */}

              <Route path="/addMember"
                element={
                  <ProtectedRoute requiredRole="ROLE_PARENT">
                    <AddMemberView />
                  </ProtectedRoute>}
              />

              <Route
                path="/userProfile"
                element={
                  <ProtectedRoute>
                    <UserProfileView />
                  </ProtectedRoute>
                }
              />

              {/** Reader detail: only parents can view any member’s activity */}
              <Route
                path="/reader/:readerId"
                element={
                  <ProtectedRoute requiredRole="ROLE_PARENT">
                    <ReaderView />
                  </ProtectedRoute>
                }
              />

            </Routes>
          </main>
        </UserContext.Provider>
      </div>
    </BrowserRouter>
  );
}
