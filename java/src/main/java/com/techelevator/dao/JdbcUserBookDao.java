package com.techelevator.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.model.UserBook;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> mapRowToUserBook(rs, rowNum));
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

        private UserBook mapRowToUserBook(ResultSet rs, int rowNum) throws SQLException {
            UserBook userBook = new UserBook();
            userBook.setUserId(rs.getInt("user_id"));
            userBook.setBookId(rs.getInt("book_id"));
            return userBook;
        }


}
