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
}
