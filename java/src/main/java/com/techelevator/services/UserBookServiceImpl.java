package com.techelevator.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techelevator.dao.UserBookDao;
import com.techelevator.model.UserBook;

@Service
public class UserBookServiceImpl implements UserBookService {

    private final UserBookDao userBookDao;

    public UserBookServiceImpl(UserBookDao userBookDao) {
        this.userBookDao = userBookDao;
    }

    @Override
    public void addUserBook(int userId, UserBook userBook) {
        userBook.setUserId(userId);

        if (!userBookDao.isBookInUserCollection(userId, userBook.getBookId())) {
            if (userBook.getDateStarted() == null) {
                userBook.setDateStarted(LocalDate.now());
            }
            userBookDao.addUserBook(userId, userBook.getBookId());
        } else {
            throw new IllegalStateException("User already linked to this book.");
        }
    }

    @Override
    public List<UserBook> getBooksByUserId(int userId) {
        return userBookDao.getUserBooks(userId);
    }
}