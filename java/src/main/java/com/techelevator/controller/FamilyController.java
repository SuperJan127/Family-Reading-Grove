package com.techelevator.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import java.security.Principal;
import java.util.List;
import com.techelevator.model.Family;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.dao.JdbcFamilyDao;
import com.techelevator.exception.DaoException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;
import com.techelevator.dao.UserDao;
import com.techelevator.model.User;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/families")
public class FamilyController {

    private final JdbcFamilyDao jdbcFamilyDao;
    private final UserDao userDao; 

    public FamilyController(JdbcFamilyDao jdbcFamilyDao, UserDao userDao) {
        this.jdbcFamilyDao = jdbcFamilyDao;
        this.userDao = userDao; 
    }

    @GetMapping
    public List<Family> getAllFamilies() {
        try {
            return jdbcFamilyDao.getAllFamilies(); 
        } catch (Exception e) {
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch families", e);
        }
    }

    @GetMapping("/{id}")
    public Family getFamilyById(@PathVariable int id) {
        Family family = jdbcFamilyDao.getFamilyById(id);
        if (family == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found");
        }
        return family;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) 
    public void addFamily(@Valid @RequestBody Family family) {
        try {
            jdbcFamilyDao.addFamily(family); 
        } catch (Exception e) {
            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add family", e);
        }
    }

    @PreAuthorize("hasRole('ROLE_PARENT')") 
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) 
    public void updateFamily(@PathVariable int id, @Valid @RequestBody Family family) {
        try {
            family.setFamilyId(id); // Set the ID for the family to update
            jdbcFamilyDao.updateFamily(family); // Updating an existing family using the DAO
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update family", e);
        }
    }

    @PreAuthorize("hasRole('ROLE_PARENT')") // Restrict access to users with the 'ROLE_PARENT' role
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Indicate that the resource has been deleted
    public void deleteFamily(@PathVariable int id) {
        try {
            jdbcFamilyDao.deleteFamily(id); // Deleting a family by its ID using the DAO
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete family", e);
        }
    }

    @GetMapping("/{id}/members")
    public List<User> getUsersByFamilyId(@PathVariable int id) {
        try {
            List<User> users = userDao.getUsersByFamilyId(id); // Fetching users by family ID
            if (users.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No members found for this family");
            }
            return users;
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch family members", e);
        }
    }

    @PreAuthorize("hasRole('ROLE_PARENT')")
    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFamilyMember(@PathVariable int id, @Valid @RequestBody RegisterUserDto newUserDto,
            Principal principal) {
        try {
            // Get the logged-in user's username
            String parentUsername = principal.getName();
            User parent = userDao.getUserByUsername(parentUsername);

            // Ensure the parent belongs to the family they're trying to modify
            if (parent == null || parent.getFamilyId() != id) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only add users to your own family.");
            }

            // Validate role
            String requestedRole = newUserDto.getRole().toUpperCase();
            if (!requestedRole.equals("ROLE_CHILD") && !requestedRole.equals("ROLE_PARENT")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Role must be either ROLE_PARENT or ROLE_CHILD.");
            }

            // Set family ID from the logged-in parent
            newUserDto.setFamilyId(id);
            newUserDto.setRole(requestedRole);

            // Optional: Check password match
            if (!newUserDto.getPassword().equals(newUserDto.getConfirmPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match.");
            }

            userDao.createUser(newUserDto);

        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create family member", e);
        }
    }


    /**
     * Endpoint to update a family member's role.
     * Only parents can update roles of children.
     */
    @GetMapping("/{id}/reading-activities")
    public List<ReadingActivity> getFamilyReadingActivities(@PathVariable int id) {
        try {
            return readingActivityDao.getActivitiesByFamilyId(id); 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch reading activities", e);
        }
    }
}
