
package com.techelevator.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Prize; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.util.List; 

/**
 * JdbcPrizeDao is a Data Access Object (DAO) that provides methods to interact with the prizes database.
 * It implements the PrizeDao interface and uses JdbcTemplate for database operations.
 */
public class JdbcPrizeDao implements PrizeDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a JdbcPrizeDao with the specified JdbcTemplate.
     *
     * @param jdbcTemplate the JdbcTemplate used for database operations
     */
    public JdbcPrizeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate; 
    }

    /**
     * Retrieves a prize by its ID.
     *
     * @param prizeId the ID of the prize to retrieve
     * @return the Prize object corresponding to the specified ID
     * @throws DaoException if an error occurs while retrieving the prize
     */
    @Override
    public Prize getPrizeById(int prizeId) {
        String sql = "SELECT * FROM prizes WHERE prize_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToPrize, prizeId);
        } catch (Exception e) {
            throw new DaoException("Error retrieving prize with ID: " + prizeId, e);
        }
    }

    /**
     * Retrieves all prizes from the database.
     *
     * @return a list of all Prize objects
     * @throws DaoException if an error occurs while retrieving the prizes
     */
    @Override
    public List<Prize> getAllPrizes() {
        String sql = "SELECT * FROM prizes";
        try {
            return jdbcTemplate.query(sql, this::mapRowToPrize);
        } catch (Exception e) {
            throw new DaoException("Error retrieving all prizes", e);
        }
    }

    /**
     * Adds a new prize to the database.
     *
     * @param prize the Prize object to add
     * @throws DaoException if an error occurs while adding the prize
     */
    @Override
    public void addPrize(Prize prize) {
        String sql = "INSERT INTO prizes (prize_name, description, minutes_required, prizes_available, start_date, end_date, user_group) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, prize.getPrizeName(), prize.getDescription(), prize.getMinutesRequired(),
                                prize.getPrizesAvailable(), prize.getStartDate(), prize.getEndDate(), prize.getUserGroup());
        } catch (Exception e) {
           throw new DaoException("Error adding prize: " + prize.getPrizeName(), e);
        }
    }

    /**
     * Updates an existing prize by its ID.
     *
     * @param prizeId the ID of the prize to update
     * @param prize the Prize object containing updated data
     * @return the updated Prize object
     * @throws DaoException if an error occurs while updating the prize
     */
    @Override   
    public Prize updatePrizeById(int prizeId, Prize prize) {
        String sql = "UPDATE prizes SET prize_name = ?, description = ?, minutes_required = ?, prizes_available = ?, " +
                     "start_date = ?, end_date = ?, user_group = ? WHERE prize_id = ?";
        try {
            jdbcTemplate.update(sql, prize.getPrizeName(), prize.getDescription(), prize.getMinutesRequired(),
                                prize.getPrizesAvailable(), prize.getStartDate(), prize.getEndDate(), 
                                prize.getUserGroup(), prizeId);
            return getPrizeById(prizeId); // Return the updated prize
        } catch (Exception e) {
            throw new DaoException("Error updating prize with ID: " + prizeId, e); 
        }
    }

    /**
     * Deletes a prize by its ID.
     *
     * @param prizeId the ID of the prize to delete
     * @return null as the method signature requires a return type
     * @throws DaoException if an error occurs while deleting the prize
     */
    @Override
    public Void deletePrizeById(int prizeId) {
        String sql = "DELETE FROM prizes WHERE prize_id = ?";
        try {
            jdbcTemplate.update(sql, prizeId);
            return null; // Return null as the method signature requires a return type
        } catch (Exception e) {
            throw new DaoException("Error deleting prize with ID: " + prizeId, e);
        }
    }

    /**
     * Maps a ResultSet row to a Prize object.
     *
     * @param rs the ResultSet containing the prize data
     * @param rowNum the number of the current row
     * @return a Prize object populated with data from the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    private Prize mapRowToPrize(ResultSet rs, int rowNum) throws SQLException {
        Prize prize = new Prize();
        prize.setPrizeId(rs.getInt("prize_id"));
        prize.setPrizeName(rs.getString("prize_name"));
        prize.setDescription(rs.getString("description"));
        prize.setMinutesRequired(rs.getInt("minutes_required"));
        prize.setPrizesAvailable(rs.getInt("prizes_available"));
        prize.setStartDate(rs.getDate("start_date").toLocalDate());
        prize.setEndDate(rs.getDate("end_date").toLocalDate());
        prize.setUserGroup(rs.getString("user_group"));
    
        return prize;
    }
}
