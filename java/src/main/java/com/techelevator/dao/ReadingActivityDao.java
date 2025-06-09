package com.techelevator.dao;

import com.techelevator.model.ReadingActivity;
import java.util.List;

public interface ReadingActivityDao {

    /**
     * Insert a new ReadingActivity (start_time is set by the DB default).
     * Returns the fully–populated object (including generated id).
     */
    ReadingActivity create(ReadingActivity readingActivity);

    /**
     * Lookup by the PK.
     */
    ReadingActivity getById(long id);

    /**
     * All sessions (past and in‐flight) for a given reader, newest first.
     */
    List<ReadingActivity> getByReaderId(long readerId);

    /**
     * In‐flight sessions (end_time IS NULL) for a reader.
     * Handy if you want to resume an open session.
     */
    List<ReadingActivity> getOpenSessionsByReaderId(long readerId);

    /**
     * Update an existing ReadingActivity (e.g. set endTime & minutesRead).
     * Returns the updated object.
     */
    ReadingActivity update(ReadingActivity readingActivity);
}
