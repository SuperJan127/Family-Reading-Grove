package com.techelevator.dao;

import java.util.List;
import com.techelevator.model.Family;
import com.techelevator.model.User;

public class FamilyDao {

    List<Family> getFamilies() {
        // Implementation to retrieve all families
        return null; // Placeholder return statement
    }
    
    Family getFamilyById(int familyId) {
        // Implementation to retrieve a family by its ID
        return null; // Placeholder return statement
    }
    Family createFamily(Family family) {
        // Implementation to create a new family
        return null; // Placeholder return statement
    }
    Family updateFamily(Family family) {
        // Implementation to update an existing family
        return null; // Placeholder return statement
    }
    Family deleteFamily(int familyId) {
        // Implementation to delete a family by its ID
        return null; // Placeholder return statement
    }
    List<Family> getFamiliesByUserId(int userId) {
        // Implementation to retrieve families associated with a specific user
        return null; // Placeholder return statement
    }
    List<Family> getFamiliesByName(String familyName) {
        // Implementation to retrieve families by their name
        return null; // Placeholder return statement
    }
    List<User> getUsersByFamilyId(int familyId) {
        // Implementation to retrieve users associated with a specific family
        return null; // Placeholder return statement
    }
    List<User> getUsersByFamilyName(String familyName) {
        // Implementation to retrieve users associated with a family by its name
        return null; // Placeholder return statement
    }
}
