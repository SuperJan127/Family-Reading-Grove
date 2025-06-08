package com.techelevator.dao;

import com.techelevator.model.Book;
import java.util.List;

public interface BookDao {

    /**
     * 
     * @param bookId the primary key of the book
     * @return the Book object with the specified ID, or null if not found
     */
    Book getBookById(int bookId);

    /**
     * 
     * @return a list of all books
     */
    List<Book> getAllBooks();

    /**
     * 
     * @param book the Book object to be added
     */
    void addBook(Book book);

}
