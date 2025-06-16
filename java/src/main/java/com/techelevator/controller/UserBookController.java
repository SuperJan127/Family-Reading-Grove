package com.techelevator.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.model.UserBook;
import com.techelevator.services.UserBookService;

@RestController
@RequestMapping("/users/{userId}/books")
@PreAuthorize("isAuthenticated()")
public class UserBookController {

    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addUserBook(@PathVariable int userId, @RequestBody UserBook userBook) {
        try {
            userBookService.addUserBook(userId, userBook);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path = "")
    public List<UserBook> getUserBooks(@PathVariable int userId) {
        return userBookService.getBooksByUserId(userId);
    }

    /**
     * GET /users/{userId}/books/completed-count
     * Return how many books this user has finished.
     */
    @GetMapping("/completed-count")
    public Map<String, Integer> getCompletedCount(@PathVariable int userId) {
        int count = userBookService.countCompletedBooksByUserId(userId);
        return Collections.singletonMap("count", count);
    }

    @PutMapping("/{bookId}/update")
    public ResponseEntity<Void> updateUserBook(
            @PathVariable int userId,
            @PathVariable int bookId,
            @RequestBody Map<String, Object> payload) {

        boolean currentlyReading = (Boolean) payload.get("currentlyReading");
        String dateFinishedStr = (String) payload.get("dateFinished");
        String notes = (String) payload.get("notes");
        int rating = (Integer) payload.get("rating");

        LocalDate dateFinished = dateFinishedStr != null ? LocalDate.parse(dateFinishedStr) : null;

        userBookService.updateUserBook(userId, bookId, currentlyReading, dateFinished, notes, rating);

        return ResponseEntity.noContent().build();
    }
}