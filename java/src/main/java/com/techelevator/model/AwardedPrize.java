
package com.techelevator.model;

import java.time.LocalDateTime;

public class AwardedPrize {
    private int awardId;
    private int prizeId;
    private Integer userId; // Nullable for family prizes
    private int familyId;
    private LocalDateTime awardedDate;
    private String prizeName;
private String username;

    // Getters and Setters
    public int getAwardId() { return awardId; }
    public void setAwardId(int awardId) { this.awardId = awardId; }

    public int getPrizeId() { return prizeId; }
    public void setPrizeId(int prizeId) { this.prizeId = prizeId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public int getFamilyId() { return familyId; }
    public void setFamilyId(int familyId) { this.familyId = familyId; }

    public LocalDateTime getAwardedDate() { return awardedDate; }
    public void setAwardedDate(LocalDateTime awardedDate) { this.awardedDate = awardedDate; }

    public String getPrizeName() { return prizeName; }
    public void setPrizeName(String prizeName) { this.prizeName = prizeName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}


