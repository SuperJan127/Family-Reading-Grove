package com.techelevator.model;

import jakarta.validation.constraints.NotBlank;

public class Family {
    private Integer familyId;
    @NotBlank
    private String familyName;

    public Family(String familyName) {
        this.familyName = familyName;
    }
    public Family() {
        // Default constructor
    }
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getFamilyId() {
        return familyId;
    }
    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }
}
