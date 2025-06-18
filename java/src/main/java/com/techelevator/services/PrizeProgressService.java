package com.techelevator.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.techelevator.dao.AwardedPrizeDao;
import com.techelevator.model.AwardedPrize;
import com.techelevator.model.Prize;
import com.techelevator.model.PrizeProgressDTO;
import com.techelevator.model.UserPrizeProgress;
import com.techelevator.model.PrizeWithUserProgressDTO;

import java.beans.Statement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrizeProgressService {

    private final JdbcTemplate jdbcTemplate;
private final AwardedPrizeDao awardedPrizeDao;

public PrizeProgressService(JdbcTemplate jdbcTemplate, AwardedPrizeDao awardedPrizeDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.awardedPrizeDao = awardedPrizeDao;
}

    public PrizeProgressDTO calculatePrizeProgress(Prize prize) {
        String sql;
        Object[] params;

        if (prize.getUserGroup().equalsIgnoreCase("family")) {
            sql = """
                SELECT SUM(ra.minutes) 
                FROM reading_activity ra
                JOIN users u ON ra.reader_id = u.user_id
                WHERE u.family_id = ?
                  AND ra.date BETWEEN ? AND ?
                """;
            params = new Object[]{prize.getFamilyId(), prize.getStartDate(), prize.getEndDate()};
        } else {
            // Placeholder for future individual logic
            return new PrizeProgressDTO(prize, 0);
        }

        Integer totalMinutes = jdbcTemplate.queryForObject(sql, Integer.class, params);
        if (totalMinutes == null) totalMinutes = 0;

        double progress = Math.min(1.0, (double) totalMinutes / prize.getMinutesRequired());

        return new PrizeProgressDTO(prize, progress);
    }

    public PrizeWithUserProgressDTO calculateUserProgressByPrize(Prize prize) {
        List<UserPrizeProgress> userProgressList = calculateIndividualProgress(prize);
    
        if ("FAMILY".equalsIgnoreCase(prize.getUserGroup())) {
            double familyProgress = calculateFamilyProgress(prize);
            userProgressList.add(0, new UserPrizeProgress(-1, "Family Total", familyProgress));
        }
    
        return new PrizeWithUserProgressDTO(prize, userProgressList);
    }
    private List<UserPrizeProgress> calculateIndividualProgress(Prize prize) {
        String sql = """
            SELECT u.user_id, u.username, COALESCE(SUM(ra.minutes), 0) AS total_minutes
            FROM users u
            LEFT JOIN reading_activity ra ON ra.reader_id = u.user_id
                AND ra.date BETWEEN ? AND ?
            WHERE u.family_id = ?
            GROUP BY u.user_id, u.username
        """;
    
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            int userId = rs.getInt("user_id");
            String username = rs.getString("username");
            int minutes = rs.getInt("total_minutes");
            double progress = Math.min(1.0, (double) minutes / prize.getMinutesRequired());
    
            return new UserPrizeProgress(userId, username, progress);
        }, prize.getStartDate(), prize.getEndDate(), prize.getFamilyId());
    }
    private double calculateFamilyProgress(Prize prize) {
        String sql = """
            SELECT SUM(ra.minutes)
            FROM reading_activity ra
            JOIN users u ON ra.reader_id = u.user_id
            WHERE u.family_id = ?
              AND ra.date BETWEEN ? AND ?
        """;
    
        Integer totalMinutes = jdbcTemplate.queryForObject(sql, Integer.class,
            prize.getFamilyId(), prize.getStartDate(), prize.getEndDate());
    
        if (totalMinutes == null) totalMinutes = 0;
    
        return Math.min(1.0, (double) totalMinutes / prize.getMinutesRequired());
    }

    public void checkAndAwardPrizes(List<Prize> prizes, int familyId) {
    for (Prize prize : prizes) {
        if ("FAMILY".equalsIgnoreCase(prize.getUserGroup())) {
            PrizeProgressDTO progressDTO = calculatePrizeProgress(prize);
            if (progressDTO.getProgress() >= 1.0 &&
                !awardedPrizeDao.hasBeenAwarded(prize.getPrizeId(), null, familyId)) {

                AwardedPrize award = new AwardedPrize();
                award.setPrizeId(prize.getPrizeId());
                award.setUserId(null);
                award.setFamilyId(familyId);
                awardedPrizeDao.awardPrize(award);
            }
        } else {
            PrizeWithUserProgressDTO grouped = calculateUserProgressByPrize(prize);
            for (UserPrizeProgress upp : grouped.getUserProgressList()) {
                if (upp.getProgress() >= 1.0 &&
                    !awardedPrizeDao.hasBeenAwarded(prize.getPrizeId(), upp.getUserId(), familyId)) {

                    AwardedPrize award = new AwardedPrize();
                    award.setPrizeId(prize.getPrizeId());
                    award.setUserId(upp.getUserId());
                    award.setFamilyId(familyId);
                    awardedPrizeDao.awardPrize(award);
                }
            }
        }
    }
}
}
