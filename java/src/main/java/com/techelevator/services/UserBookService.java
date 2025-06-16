package com.techelevator.services;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.model.UserBook;

public interface UserBookService {
    
    void addUserBook(int userId, UserBook userBook);

    List<UserBook> getBooksByUserId(int userId);

    int countCompletedBooksByUserId(int userId);

    public void updateUserBook(int userId, int bookId, boolean currentlyReading, LocalDate dateFinished, String notes, int rating);
}
