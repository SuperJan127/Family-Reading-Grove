package com.techelevator.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.techelevator.model.Prize;
import com.techelevator.model.PrizeProgressDTO;
import com.techelevator.model.UserPrizeProgress;
import com.techelevator.model.PrizeWithUserProgressDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrizeProgressService {

    private final JdbcTemplate jdbcTemplate;

    public PrizeProgressService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        if (!prize.getUserGroup().equalsIgnoreCase("family")) {
            return new PrizeWithUserProgressDTO(prize, List.of()); // Not handling individual prizes yet
        }
    
        String sql = """
            SELECT u.user_id, u.username, COALESCE(SUM(ra.minutes), 0) AS total_minutes
            FROM users u
            LEFT JOIN reading_activity ra ON ra.reader_id = u.user_id
                AND ra.date BETWEEN ? AND ?
            WHERE u.family_id = ?
            GROUP BY u.user_id, u.username
            """;
    
        List<UserPrizeProgress> progressList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            int userId = rs.getInt("user_id");
            String username = rs.getString("username");
            int minutes = rs.getInt("total_minutes");
    
            double progress = Math.min(1.0, (double) minutes / prize.getMinutesRequired());
            return new UserPrizeProgress(userId, username, progress);
        }, prize.getStartDate(), prize.getEndDate(), prize.getFamilyId());
    
        return new PrizeWithUserProgressDTO(prize, progressList);
    }
}
