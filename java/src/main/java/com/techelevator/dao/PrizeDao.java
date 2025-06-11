package com.techelevator.dao;

import com.techelevator.model.Prize;
import java.util.List;



public interface PrizeDao {

    /**
     * Retrieves a Prize by its ID.
     *
     * @param prizeId the ID of the prize
     * @return the Prize object, or null if not found
     */
    Prize getPrizeById(int prizeId);

    /**
     * Retrieves all available prizes.
     *
     * @return a list of all available prizes
     */
    List<Prize> getAllPrizes();

    /**
     * Adds a new prize to the system.
     *
     * @param prize the Prize object to be added
     */
    void addPrize(Prize prize);

    /**
     * Updates the details of a prize identified by its ID.
     *
     * @param prizeId the ID of the prize to be updated
     * @param prize the Prize object containing the updated details
     * @return the updated Prize object
     * @throws IllegalArgumentException if the prizeId is invalid or if the prize object is null
     */
    Prize updatePrizeById(int prizeId, Prize prize);

    /**
     * Deletes a prize from the database based on the provided prize ID.
     *
     * @param prizeId the ID of the prize to be deleted
     * @return Void
     */
    Void deletePrizeById(int prizeId);

}
