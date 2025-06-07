package com.techelevator.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.techelevator.exception.DaoException;
import com.techelevator.model.RegisterUserDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.techelevator.model.User;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT user_id, username, password_hash, role, family_id FROM users WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash, role, family_id FROM users";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                users.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");
        User user = null;
        String sql = "SELECT user_id, username, password_hash, role, family_id FROM users WHERE username = LOWER(TRIM(?));";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
            if (rowSet.next()) {
                user = mapRowToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    public User createUser(RegisterUserDto user) {
        User newUser = null;
        String password_hash = new BCryptPasswordEncoder().encode(user.getPassword());
        String ssRole = user.getRole().toUpperCase().startsWith("ROLE_") ? user.getRole().toUpperCase() : "ROLE_" + user.getRole().toUpperCase();
    
        try {
            Integer familyId = user.getFamilyId();
    
            // ✅ If familyId not provided, create a new family with the given name
            if (familyId == null) {
                String familyName = user.getNewFamilyName();
    
                if (familyName == null || familyName.trim().isEmpty()) {
                    throw new DaoException("A new family name is required when no familyId is provided.");
                }
    
                String insertFamilySql = "INSERT INTO families (family_name) VALUES (?) RETURNING family_id";
                familyId = jdbcTemplate.queryForObject(insertFamilySql, Integer.class, familyName.trim());
            }
    
            // ✅ Now insert the user with the resolved familyId
            String insertUserSql = "INSERT INTO users (username, password_hash, role, family_id) VALUES (LOWER(TRIM(?)), ?, ?, ?) RETURNING user_id";
            int newUserId = jdbcTemplate.queryForObject(
                insertUserSql,
                Integer.class,
                user.getUsername(), password_hash, ssRole, familyId);
    
            newUser = getUserById(newUserId);
    
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    
        return newUser;
    }
     
    public User updateUserFamilyId(int userId, Integer familyId) {
        String sql = "UPDATE users SET family_id = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, familyId, userId);
            return getUserById(userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setRole(Objects.requireNonNull(rs.getString("role")));
        user.setActivated(true);
        user.setFamilyId(rs.getInt("family_id"));
        return user;
    }
}
