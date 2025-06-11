
package com.techelevator.model;

import java.time.LocalDate;

public class Prize {

    private int prizeId;
    private String prizeName;
    private String description;
    private int minutesRequired;
    private int prizesAvailable;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userGroup;

    public Prize(int prizeId, String prizeName, String description, int minutesRequired, int prizesAvailable,
            LocalDate startDate, LocalDate endDate, String userGroup) {
        this.prizeId = prizeId;
        this.prizeName = prizeName;
        this.description = description;
        this.minutesRequired = minutesRequired;
        this.prizesAvailable = prizesAvailable;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userGroup = userGroup;
    }

    public Prize() {
        // Default constructor
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinutesRequired() {
        return minutesRequired;
    }

    public void setMinutesRequired(int minutesRequired) {
        this.minutesRequired = minutesRequired;
    }

    public int getPrizesAvailable() {
        return prizesAvailable;
    }

    public void setPrizesAvailable(int prizesAvailable) {
        this.prizesAvailable = prizesAvailable;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

}