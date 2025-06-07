package com.techelevator.services;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.techelevator.dao.UserDao; 

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.techelevator.model.User user = userDao.getUserByUsername(username); // Your user class

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert role from DB into a Spring GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(), // Changed from getPasswordHash() to getPassword()
            List.of(authority)
        );
    }
}