package com.techelevator.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techelevator.dao.BookDao;
import com.techelevator.dao.UserBookDao;
import com.techelevator.model.Book;
import com.techelevator.model.UserBook;

@Service
public class UserBookServiceImpl implements UserBookService {

    private final UserBookDao userBookDao;
    private final BookDao bookDao;

    public UserBookServiceImpl(UserBookDao userBookDao, BookDao bookDao) {
        this.userBookDao = userBookDao;
        this.bookDao = bookDao;
    }

    @Override
    public void addUserBook(int userId, UserBook userBook) {
        Book book = userBook.getBook(); // this is the nested book

        if (book == null) {
            throw new IllegalArgumentException("Book is required");
        }

        // Look up the book in the DB
        Book existingBook = bookDao.getBookByIsbn(book.getIsbn());
        int bookId;

        if (existingBook != null) {
            bookId = existingBook.getBookId();
        } else {
            bookDao.addBook(book); // insert new book
            existingBook = bookDao.getBookByIsbn(book.getIsbn());

            if (existingBook == null) {
                throw new IllegalStateException("Book insert failed");
            }

            bookId = existingBook.getBookId();
        }

        userBookDao.addUserBook(
                userId,
                bookId,
                userBook.isCurrentlyReading(),
                userBook.getDateStarted(),
                userBook.getDateFinished());
    }

    @Override
    public List<UserBook> getBooksByUserId(int userId) {
        return userBookDao.getUserBooks(userId);
    }

    @Override
    public int countCompletedBooksByUserId(int userId) {
        return userBookDao.countCompletedBooksByUserId(userId);
    }

    @Override
public void updateUserBook(int userId, int bookId, boolean currentlyReading, LocalDate dateFinished, String notes, int rating) {
    userBookDao.updateUserBook(userId, bookId, currentlyReading, dateFinished, notes, rating);
}
}