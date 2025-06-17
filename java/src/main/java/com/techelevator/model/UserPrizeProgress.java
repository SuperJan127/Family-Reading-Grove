package com.techelevator.model;

public class UserPrizeProgress {
    private int userId;
    private String username;
    private double progress;

    public UserPrizeProgress(int userId, String username, double progress) {
        this.userId = userId;
        this.username = username;
        this.progress = progress;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public double getProgress() {
        return progress;
    }
}
