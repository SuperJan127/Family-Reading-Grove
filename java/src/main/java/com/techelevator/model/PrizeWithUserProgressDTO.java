package com.techelevator.model;

import java.util.List;
import com.techelevator.model.Prize;

public class PrizeWithUserProgressDTO {
    private Prize prize;
    private List<UserPrizeProgress> userProgressList;

    public PrizeWithUserProgressDTO(Prize prize, List<UserPrizeProgress> userProgressList) {
        this.prize = prize;
        this.userProgressList = userProgressList;
    }

    public Prize getPrize() {
        return prize;
    }

    public List<UserPrizeProgress> getUserProgressList() {
        return userProgressList;
    }
}
