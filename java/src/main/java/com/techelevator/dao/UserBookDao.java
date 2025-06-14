package com.techelevator.dao;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.model.User;
import com.techelevator.model.UserBook;

public interface UserBookDao {
    /**
     * Adds a book to a user's collection.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book to add
     */
    void addUserBook(int userId, int bookId, boolean currentlyReading, LocalDate dateStarted, LocalDate dateFinished);

    /**
     * Retrieves a list of books in a user's collection.
     *
     * @param userId the ID of the user
     * @return a list of UserBook objects representing the user's collection
     */
    List<UserBook> getUserBooks(int userId);

    /**
     * Checks if a book is in a user's collection.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book to check
     * @return true if the book is in the user's collection, false otherwise
     */
    boolean isBookInUserCollection(int userId, int bookId);

    /**
     * Removes a book from a user's collection.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book to remove
     */
    void removeUserBook(int userId, int bookId);

    /**
     * Retrieves only the books a user is actively reading.
     *
     * @param userId the ID of the user
     * @return a list of UserBook objects where currentlyReading == true
     */
    List<UserBook> getCurrentBooksByUserId(int userId);

    /**
     * Counts how many books a user has completed.
     *
     * @param userId the ID of the user
     * @return number of completed books
     */
    int countCompletedBooksByUserId(int userId);

    void updateUserBook(int user_id, int book_id, boolean currentlyReading, LocalDate dateFinished, String notes, int rating);

}
