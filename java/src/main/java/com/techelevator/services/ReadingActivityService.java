package com.techelevator.services;

import com.techelevator.model.Format;

/**
 * Service API for recording reading activities.
 */
public interface ReadingActivityService {

    /**
     * Record a new reading activity.
     *
     * @param readerId the ID of the reader
     * @param bookId   the ID of the book
     * @param format   the reading format (e.g. PAPER, DIGITAL, AUDIOBOOK, etc.)
     * @param minutes  the number of minutes spent reading
     * @param notes    optional notes about the session
     */
    void recordActivity(long readerId,
            long bookId,
            Format format,
            int minutes,
            String notes);
}
// Note: This service interface is designed to be implemented by a class that
// interacts with a DAO to persist the reading activity data.
