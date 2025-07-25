package com.techelevator.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;

import com.techelevator.model.Book;
import com.techelevator.model.UserBook;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class JdbcUserBookDao implements UserBookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserBookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUserBook(int userId, int bookId, boolean currentlyReading, LocalDate dateStarted, LocalDate dateFinished) {
        String sql = "INSERT INTO user_book (user_id, book_id, currently_reading, date_started, date_completed) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, userId, bookId, currentlyReading, dateStarted, dateFinished);
    }

    @Override
    public List<UserBook> getUserBooks(int userId) {
        String sql = """
            SELECT ub.user_id, ub.book_id, ub.currently_reading, ub.date_started, ub.date_completed,
                   ub.notes, ub.rating,
                   b.title, b.author, b.isbn
            FROM user_book ub
            JOIN books b ON ub.book_id = b.book_id
            WHERE ub.user_id = ?
        """;
        return jdbcTemplate.query(sql, mapRowToUserBook(), userId);
    }

    @Override
    public boolean isBookInUserCollection(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM user_book WHERE user_id = ? AND book_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, bookId);
        return count != null && count > 0;
    }

    @Override
    public void removeUserBook(int userId, int bookId) {
        String sql = "DELETE FROM user_book WHERE user_id = ? AND book_id = ?";
        jdbcTemplate.update(sql, userId, bookId);
    }

    @Override
    public List<UserBook> getCurrentBooksByUserId(int userId) {
        String sql = "SELECT ub.user_id, ub.book_id, ub.currently_reading, ub.date_started, ub.date_completed,\n" + //
                        "                             ub.notes, ub.rating,\n" + //
                        "                             b.title, b.author, b.isbn\n" + //
                        "                      FROM user_book ub\n" + //
                        "                      JOIN books b ON ub.book_id = b.book_id\n" + //
                        "                      WHERE ub.user_id = ? AND currently_reading = TRUE";
        return jdbcTemplate.query(sql, mapRowToUserBook(), userId);
    }

    @Override
    public int countCompletedBooksByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM user_book WHERE user_id = ? AND currently_reading = FALSE";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    @Override
    public void updateUserBook(int userId, int bookId, boolean currentlyReading, LocalDate dateFinished, String notes, int rating) {
        String sql = "UPDATE user_book SET currently_reading = ?, date_completed = ?, notes = ?, rating = ? WHERE user_id = ? AND book_id = ?";
        jdbcTemplate.update(sql, currentlyReading, dateFinished, notes, rating, userId, bookId);
        
    }

    private RowMapper<UserBook> mapRowToUserBook() {
        return (rs, rowNum) -> {
            UserBook userBook = new UserBook();
            userBook.setUserId(rs.getInt("user_id"));
            userBook.setBookId(rs.getInt("book_id"));
            userBook.setCurrentlyReading(rs.getBoolean("currently_reading"));
            if (rs.getDate("date_started") != null) {
                userBook.setDateStarted(rs.getDate("date_started").toLocalDate());
            }
            if (rs.getDate("date_completed") != null) {
                userBook.setDateFinished(rs.getDate("date_completed").toLocalDate());
            }
            userBook.setNotes(rs.getString("notes"));
            userBook.setRating(rs.getInt("rating"));

            Book book = new Book();
            book.setBookId(rs.getInt("book_id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setIsbn(rs.getString("isbn"));

            userBook.setBook(book);
            return userBook;
        };
    }
}
