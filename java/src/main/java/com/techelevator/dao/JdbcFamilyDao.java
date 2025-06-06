package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Family;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

@Component
public class JdbcFamilyDao {

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

    public void updateFamily(Family family) {
        String sql = "UPDATE families SET family_name = ? WHERE family_id = ?";
        try {
            jdbcTemplate.update(sql, family.getFamilyName(), family.getFamilyId());
        } catch (Exception e) {
            // Handle exception (optional)
        }
    }

    public void deleteFamily(int id) {
        String sql = "DELETE FROM families WHERE family_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            // Handle exception (optional)
        }
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
}