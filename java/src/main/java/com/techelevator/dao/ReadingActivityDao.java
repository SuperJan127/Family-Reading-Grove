package com.techelevator.dao;

import com.techelevator.model.ReadingActivity;
import java.util.List;

public interface ReadingActivityDao {
    /**
     * Insert a new reading activity into the database.
     */
    void recordReadingTime(ReadingActivity activity);

    /**
     * Retrieve all reading activities for a given reader.
     */
    List<ReadingActivity> getReadingHistory(long readerId);

    /**
     * Returns all reading activity for every member of a given family.
     *
     * @param familyId the ID of the family
     * @return a list of ReadingActivity objects for the family
     */
    List<ReadingActivity> getActivitiesByFamilyId(int familyId);

    /**
     * Calculate the total minutes read by a specific user.
     *
     * @param userId the ID of the user
     * @return sum of minutes read by the user
     */
    int getTotalMinutesByUserId(int userId);
}
