package com.techelevator.services;

import java.util.List;

import com.techelevator.model.UserBook;

public interface UserBookService {
    
    void addUserBook(int userId, UserBook userBook);

    List<UserBook> getBooksByUserId(int userId);
}
