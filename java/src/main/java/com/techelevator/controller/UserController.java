package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.dao.ReadingActivityDao;
import com.techelevator.dao.UserBookDao;
import com.techelevator.model.UserReadingSummaryDto;

import com.techelevator.exception.DaoException;
import com.techelevator.model.FamilyIdDto;
import com.techelevator.model.User;
import com.techelevator.model.UserBook;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * The UserController is a class for handling HTTP Requests related to getting
 * User information.
 *
 * It depends on an instance of a UserDAO for retrieving and storing data. This
 * is provided
 * through dependency injection.
 *
 * Note: This class does not handle authentication (registration/login) of
 * Users. That is
 * handled separately in the AuthenticationController.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/users")
public class UserController {

    private UserDao userDao;
    private UserBookDao userBookDao;
    private ReadingActivityDao readingActivityDao;

    public UserController(UserDao userDao, UserBookDao userBookDao, ReadingActivityDao readingActivityDao) {
        this.userDao = userDao;
        this.userBookDao = userBookDao;
        this.readingActivityDao = readingActivityDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            users = userDao.getUsers();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return users;
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User getById(@PathVariable int userId, Principal principal) {
        User user = null;

        try {
            user = userDao.getUserById(userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return user;
    }

    @PutMapping("/{userId}/familyId")
    public ResponseEntity<Void> updateUserFamilyId(@PathVariable int userId, @RequestBody FamilyIdDto dto) {
        try {
            userDao.updateUserFamilyId(userId, dto.getFamilyId());
            return ResponseEntity.noContent().build();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /users/{userId}/summary
     * Returns completed-book count, total minutes read,
     * and current reading list for the given user.
     */
    @GetMapping("/{userId}/summary")
    public UserReadingSummaryDto getUserSummary(@PathVariable int userId) {
        int completedBooks = userBookDao.countCompletedBooksByUserId(userId);
        int totalMinutes = readingActivityDao.getTotalMinutesByUserId(userId);
        List<UserBook> current = userBookDao.getCurrentBooksByUserId(userId);

        return new UserReadingSummaryDto(completedBooks, totalMinutes, current);
    }

}
