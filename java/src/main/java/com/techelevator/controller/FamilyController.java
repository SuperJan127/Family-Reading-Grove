package com.techelevator.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.List; // Importing List
import com.techelevator.model.Family; // Assuming Family is a model class representing a family
import com.techelevator.model.RegisterUserDto;
import com.techelevator.dao.JdbcFamilyDao; // Importing JdbcFamilyDao
import com.techelevator.exception.DaoException;

import org.springframework.web.bind.annotation.GetMapping; // Importing GetMapping
import org.springframework.web.bind.annotation.PathVariable; // Importing PathVariable
import org.springframework.web.server.ResponseStatusException; // Importing ResponseStatusException
import org.springframework.http.HttpStatus; // Importing HttpStatus
import org.springframework.web.bind.annotation.PostMapping; // Importing PostMapping
import org.springframework.web.bind.annotation.PutMapping; // Importing PutMapping
import org.springframework.web.bind.annotation.DeleteMapping; // Importing DeleteMapping
import org.springframework.web.bind.annotation.RequestBody; // Importing RequestBody
import org.springframework.web.bind.annotation.ResponseStatus; // Importing ResponseStatus
import jakarta.validation.Valid; // Importing Valid for validation annotations
import com.techelevator.dao.UserDao; // Importing UserDao for user operations
import com.techelevator.model.User; // Importing User class

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/families")
public class FamilyController {

    private final JdbcFamilyDao jdbcFamilyDao;
    private final UserDao userDao; // ✅ Added this

    public FamilyController(JdbcFamilyDao jdbcFamilyDao, UserDao userDao) {
        this.jdbcFamilyDao = jdbcFamilyDao;
        this.userDao = userDao; // ✅ Set it here
    }
    @GetMapping
    public List<Family> getAllFamilies() {
        try {
            return jdbcFamilyDao.getAllFamilies(); // Fetching the list of families from the DAO
        } catch (Exception e) {
            // Handle exception (e.g., log it)
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
    @ResponseStatus(HttpStatus.CREATED) // Indicate that a resource has been created
    public void addFamily(@Valid @RequestBody Family family) {
        try {
            jdbcFamilyDao.addFamily(family); // Adding a new family using the DAO
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add family", e);
        }
    }

    @PreAuthorize("hasRole('ROLE_PARENT')") // Restrict access to users with the 'ROLE_PARENT' role
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Indicate that the request was successful
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

}
