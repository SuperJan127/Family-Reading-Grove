package com.techelevator.controller;

import java.util.List;

import com.techelevator.dao.ReadingActivityDao;
import com.techelevator.model.ReadingActivity;
import com.techelevator.model.UserMinutesDTO;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/reading-activities")
public class ReadingActivityController {

    private final ReadingActivityDao activityDao;

    public ReadingActivityController(ReadingActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    /**
     * GET /reading-activities
     * 
     * @return all recorded reading activities
     */
    @GetMapping
    public List<ReadingActivity> getAllReadingActivities() {
        try {
            // If you want to expose every record, implement a dao.getAllReadingActivities()
            // For now, you might return an empty list or throw unsupported if not needed:
            throw new UnsupportedOperationException("Fetching all activities not supported");
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch activities", e);
        }
    }

    /**
     * GET /reading-activities/reader/{readerId}
     * 
     * @param readerId the user whose history to fetch
     * @return that reader’s history
     */
    @GetMapping("/reader/{readerId}")
    public List<ReadingActivity> getReadingHistory(@PathVariable long readerId) {
        try {
            return activityDao.getReadingHistory(readerId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch reading history", e);
        }
    }

    /**
     * POST /reading-activities
     * Accepts a JSON body matching ReadingActivity (readerId, bookId, format,
     * minutes, notes)
     * and persists it.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReadingActivity(@Valid @RequestBody ReadingActivity activity) {
        try {
            activityDao.recordReadingTime(activity);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Unable to record reading activity", e);
        }
    }

    /**
     * GET /reading-activities/family/{familyId}
     * 
     * @return all reading activities for that family
     */
    @GetMapping("/family/{familyId}")
    public List<ReadingActivity> getByFamily(@PathVariable int familyId) {
        try {
            return activityDao.getActivitiesByFamilyId(familyId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to fetch family reading history",
                    e);
        }
    }

    /**
     * GET /reading-activities/reader/{readerId}/total-minutes
     * 
     * @return total minutes read by that user
     */
    @GetMapping("/reader/{readerId}/total-minutes")
    public int getTotalMinutesByUser(@PathVariable int readerId) {
        try {
            return activityDao.getTotalMinutesByUserId(readerId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to calculate total minutes for user",
                    e);
        }
    }

    /**
     * GET /reading-activities/family/{familyId}/minutes-by-user
     * 
     * @return list of total minutes read per user in the family
     */
    @GetMapping("/family/{familyId}/minutes-by-user")
    public List<UserMinutesDTO> getFamilyReadingMinutesByUser(@PathVariable int familyId) {
        try {
            return activityDao.getTotalMinutesByUserInFamily(familyId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to get total minutes per user for family",
                    e);
        }
    }

    /**
     * GET /reading-activities/family/{familyId}/total-minutes
     * 
     * @return total minutes read by the whole family
     */
    @GetMapping("/family/{familyId}/total-minutes")
    public int getTotalMinutesByFamily(@PathVariable int familyId) {
        try {
            return activityDao.getTotalMinutesByFamilyId(familyId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to calculate total minutes for family",
                    e);
        }
    }

}
