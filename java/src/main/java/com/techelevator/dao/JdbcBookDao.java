package com.techelevator.dao;

import java.util.ArrayList;
import java.util.List;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Book;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBookDao implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate; // Constructor injection of JdbcTemplate
    }

    // Implement the methods from the BookDao interface here

    @Override
    public Book getBookById(int bookId) {
        // Logic to retrieve a book by its ID from the database
        try {
            String sql = "SELECT book_id, title, author, isbn FROM books WHERE book_id = ?";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, bookId);
            if (rs.next()) {
                return mapRowToBook(rs); // Map the row to a Book object
            }
            return null; // Return null if no book is found with the given ID
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database connection error", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public List<Book> getAllBooks() {
        // Logic to retrieve all books from the database
        List<Book> books = new ArrayList<>();
        String sql = "SELECT book_id, title, author, isbn FROM books";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                books.add(mapRowToBook(rs)); // Map each row to a Book object and add to the list
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database connection error", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return books; // Return the list of books
    }

    @Override
    public void addBook(Book book) {
        // Logic to add a new book to the database
        try {
            String sql = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getIsbn());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database connection error", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);

        }
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        // Logic to retrieve a book by its ISBN from the database
        try {
            String sql = "SELECT book_id, title, author, isbn FROM books WHERE isbn = ?";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, isbn);
            if (rs.next()) {
                return mapRowToBook(rs); // Map the row to a Book object
            }
            return null; // Return null if no book is found with the given ISBN
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database connection error", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
    
    private Book mapRowToBook(SqlRowSet rs) {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        return book;
    }

}
