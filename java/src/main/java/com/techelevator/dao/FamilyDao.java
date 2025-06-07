package com.techelevator.dao;

import java.util.List;
import com.techelevator.model.Family;
import com.techelevator.model.User;

public interface FamilyDao {

    List<Family> getFamilies();
    
    Family getFamilyById(int familyId);
    
    Family createFamily(Family family);
    
    Family updateFamily(Family family);
    
    Family deleteFamily(int familyId);
    
    List<Family> getFamiliesByUserId(int userId);
    
    List<Family> getFamiliesByName(String familyName);
    
    List<User> getUsersByFamilyId(int familyId);
    
    List<User> getUsersByFamilyName(String familyName);
}
