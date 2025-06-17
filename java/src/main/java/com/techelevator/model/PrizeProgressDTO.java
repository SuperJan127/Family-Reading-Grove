package com.techelevator.model;

public class PrizeProgressDTO {
    private Prize prize;
    private double progress; // e.g., 0.75 for 75%

    public PrizeProgressDTO(Prize prize, double progress) {
        this.prize = prize;
        this.progress = progress;
    }

    public Prize getPrize() {
        return prize;
    }

    public double getProgress() {
        return progress;
    }
}