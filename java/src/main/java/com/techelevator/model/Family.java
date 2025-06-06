package com.techelevator.model;

public class Family {
    private int familyId;
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
