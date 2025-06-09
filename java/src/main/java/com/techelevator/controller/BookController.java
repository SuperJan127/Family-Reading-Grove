package com.techelevator.controller;
import java.util.List;

import com.techelevator.dao.BookDao;
import com.techelevator.model.Book;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/books")

public class BookController {
    // This class will handle HTTP requests related to books
    // It will use the BookDao to interact with the database

    private final BookDao bookDao; // DAO for book operations
    public BookController(BookDao bookDao) {
        this.bookDao = bookDao; // Constructor injection of BookDao
    }

    @GetMapping
    public List<Book> getAllBooks() {
        try {
            return bookDao.getAllBooks(); // Fetching the list of books from the DAO
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch books", e);
        }
    }
    
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        Book book = bookDao.getBookById(id);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return book;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indicate that a resource has been created
    public void addBook(@Valid @RequestBody Book book) {
        //  handle making sure book doesn't already exist!!!!!

        try {
            bookDao.addBook(book); // Adding a new book using the DAO
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add book", e);
        }
    }

    // Additional methods for updating and deleting books can be added here
}
