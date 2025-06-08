package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Family;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.techelevator.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

@Component
public class JdbcFamilyDao implements FamilyDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFamilyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Family> getAllFamilies() {
        String sql = "SELECT * FROM families";
        try {
            return jdbcTemplate.query(sql, new FamilyRowMapper());
        } catch (Exception e) {
            return Collections.emptyList(); // Return null if an exception occurs
        }
    }

    public Family getFamilyById(int id) {
        String sql = "SELECT * FROM families WHERE family_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new FamilyRowMapper(), id);
        } catch (Exception e) {
            return null; // Return null if an exception occurs
        }
    }

    public void addFamily(Family family) {
        String sql = "INSERT INTO families (family_name) VALUES (?) RETURNING family_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, family.getFamilyName());
            family.setFamilyId(newId);
        } catch (Exception e) {
            throw new DaoException("Error creating family", e); // Throw a custom exception if an error occurs
        }
    }

    public Family updateFamily(Family family) {
        String sql = "UPDATE families SET family_name = ? WHERE family_id = ?";
        try {
            jdbcTemplate.update(sql, family.getFamilyName(), family.getFamilyId());
            return family; // Return the updated family object
        } catch (Exception e) {
            return null; // Return null in case of an exception
        }
    }

    @Override
    public void deleteFamily(int id) {
        String sql = "DELETE FROM families WHERE family_id = ?";
        try {
            jdbcTemplate.update(sql, id); // Perform the delete operation
        } catch (Exception e) {
            // Handle exception (optional)
        }
    }

    @Override
    public Family createFamily(Family family) {
        addFamily(family); // Reuse the addFamily method
        return family; // Return the created family object
    }

    @Override
    public List<Family> getFamiliesByUserId(int userId) {
        String sql = "SELECT * FROM families WHERE user_id = ?";
        return jdbcTemplate.query(sql, new FamilyRowMapper(), userId);
    }

    @Override
    public List<Family> getFamilies() {
        return getAllFamilies(); // Reuse the getAllFamilies method
    }

    @Override
    public List<Family> getFamiliesByName(String familyName) {
        String sql = "SELECT * FROM families WHERE family_name = ?";
        return jdbcTemplate.query(sql, new FamilyRowMapper(), familyName);
    }

    
    

    private static class FamilyRowMapper implements RowMapper<Family> {
        @Override
        public Family mapRow(ResultSet rs, int rowNum) throws SQLException {
            Family family = new Family();
            family.setFamilyId(rs.getInt("family_id"));
            family.setFamilyName(rs.getString("family_name"));
            return family;
        }
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            return user;
        }
    }
}