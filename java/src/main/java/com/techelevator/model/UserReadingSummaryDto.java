package com.techelevator.model;

import java.util.List;

/**
 * DTO representing a user’s reading summary for display purposes.
 */
public class UserReadingSummaryDto {

    private int completedBooks;
    private int totalMinutes;
    private List<UserBook> currentBooks;

    public UserReadingSummaryDto(int completedBooks, int totalMinutes, List<UserBook> currentBooks) {
        this.completedBooks = completedBooks;
        this.totalMinutes = totalMinutes;
        this.currentBooks = currentBooks;
    }

    public UserReadingSummaryDto() {
        // default constructor
    }

    public int getCompletedBooks() {
        return completedBooks;
    }

    public void setCompletedBooks(int completedBooks) {
        this.completedBooks = completedBooks;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public List<UserBook> getCurrentBooks() {
        return currentBooks;
    }

    public void setCurrentBooks(List<UserBook> currentBooks) {
        this.currentBooks = currentBooks;
    }
}





