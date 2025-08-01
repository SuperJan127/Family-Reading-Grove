package com.techelevator.security;

import com.techelevator.model.Authority;
import com.techelevator.model.User;
import com.techelevator.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

    private final UserDao userDao;

    public UserModelDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating user '{}'", login);

        String lowercaseLogin = login.toLowerCase();

        User user = userDao.getUserByUsername(lowercaseLogin);
        if (user != null) {
            return createSpringSecurityUser(lowercaseLogin, user);
        } else {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found.");
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
    
        // Create a single GrantedAuthority from the user's role string
        List<GrantedAuthority> grantedAuthorities = List.of(
            new SimpleGrantedAuthority(user.getRole()) // e.g. "ROLE_PARENT"
        );
    
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            grantedAuthorities
        );
    }
}
