package com.techelevator.dao;

import java.util.List;
import com.techelevator.model.UserBook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserBookDao implements UserBookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserBookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUserBook(int userId, int bookId) {
        String sql = "INSERT INTO user_books (user_id, book_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, bookId);
    }

    @Override
    public List<UserBook> getUserBooks(int userId) {
        String sql = "SELECT * FROM user_books WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[] { userId }, (rs, rowNum) -> mapRowToUserBook(rs, rowNum));
    }

    @Override
    public boolean isBookInUserCollection(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM user_books WHERE user_id = ? AND book_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, bookId);
        return count != null && count > 0;
    }

    @Override
    public void removeUserBook(int userId, int bookId) {
        String sql = "DELETE FROM user_books WHERE user_id = ? AND book_id = ?";
        jdbcTemplate.update(sql, userId, bookId);
    }

    @Override
    public List<UserBook> getCurrentBooksByUserId(int userId) {
        String sql = "SELECT id, user_id, book_id, currently_reading, date_started, date_completed " +
                     "FROM user_book WHERE user_id = ? AND currently_reading = TRUE";
        return jdbcTemplate.query(sql, mapRowToUserBook(), userId);
    }

    @Override
    public int countCompletedBooksByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM user_book WHERE user_id = ? AND currently_reading = FALSE";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    private RowMapper<UserBook> mapRowToUserBook() {
        return (rs, rowNum) -> {
            UserBook ub = new UserBook();
            ub.setUserId(rs.getInt("user_id"));
            ub.setBookId(rs.getInt("book_id"));
            ub.setCurrentlyReading(rs.getBoolean("currently_reading"));
            ub.setDateStarted(rs.getDate("date_started").toLocalDate());
            ub.setDateFinished(rs.getDate("date_completed").toLocalDate());
            return ub;
        };
    }

}
