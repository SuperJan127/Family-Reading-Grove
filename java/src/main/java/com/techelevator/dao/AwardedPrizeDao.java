package com.techelevator.dao;

import com.techelevator.model.AwardedPrize;
import java.util.List;

public interface AwardedPrizeDao {
    void awardPrize(AwardedPrize award);
    boolean hasBeenAwarded(int prizeId, Integer userId, int familyId);
    List<AwardedPrize> getAwardsByFamilyId(int familyId);
}
