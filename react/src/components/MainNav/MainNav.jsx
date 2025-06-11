import { Link, NavLink } from 'react-router-dom';
import { useContext } from 'react';
import { UserContext } from '../../context/UserContext';
import styles from './MainNav.module.css';

export default function MainNav() {
  const { user } = useContext(UserContext);
  const isParent = user?.role === 'ROLE_PARENT';
  const isChild = user?.role === 'ROLE_CHILD';


  return (
    <nav id="main-nav" className="nav-list">
      <div className="nav-link">
        <li className="nav-link">
          <NavLink to={
            isParent ? '/parent' :
              isChild ? '/child' :
                '/'
          }>
            Home
          </NavLink>
        </li>

      </div>
      {user ? (
        <>
        

          <div className="nav-link">
            <NavLink to="/addReading">
              Read
            </NavLink>
          </div>

          <div className="nav-link">
            <NavLink to="/books">
              Book List
            </NavLink>
          </div>

          <div className="nav-link">
            <Link to="/logout">
              Logout
            </Link>
          </div>
        </>
      ) : (
        <div className="nav-link">
          <NavLink to="/login">
            Login
          </NavLink>
        </div>
      )}
    </nav>
  );
}
