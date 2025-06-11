package com.techelevator.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<?> addUserBook(@PathVariable int userId, @RequestBody UserBook userBook) {
        try {
            userBookService.addUserBook(userId, userBook);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public List<UserBook> getUserBooks(@PathVariable int userId) {
        return userBookService.getBooksByUserId(userId);
    }
}