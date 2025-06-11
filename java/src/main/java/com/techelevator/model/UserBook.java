package com.techelevator.model;

import java.time.LocalDate;

public class UserBook {
    private int userId;
    private int bookId;
    private boolean currentlyReading;
    private LocalDate dateStarted;
    private LocalDate dateFinished;

    public UserBook(int userId, int bookId, boolean currentlyReading, LocalDate dateStarted, LocalDate dateFinished) {
        this.userId = userId;
        this.bookId = bookId;
        this.currentlyReading = currentlyReading;
        this.dateStarted = dateStarted;
        this.dateFinished = dateFinished;
    }

    public UserBook() {
        // Default constructor
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isCurrentlyReading() {
        return currentlyReading;
    }

    public void setCurrentlyReading(boolean currentlyReading) {
        this.currentlyReading = currentlyReading;
    }

    public LocalDate getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDate getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(LocalDate dateFinished) {
        this.dateFinished = dateFinished;
    }
}
