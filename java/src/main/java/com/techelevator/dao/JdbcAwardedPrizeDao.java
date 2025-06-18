package com.techelevator.dao;

import com.techelevator.model.AwardedPrize;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcAwardedPrizeDao implements AwardedPrizeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAwardedPrizeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void awardPrize(AwardedPrize award) {
        String sql = """
            INSERT INTO awarded_prizes (prize_id, user_id, family_id)
            VALUES (?, ?, ?)
            """;
        jdbcTemplate.update(sql, award.getPrizeId(), award.getUserId(), award.getFamilyId());
    }

    @Override
    public boolean hasBeenAwarded(int prizeId, Integer userId, int familyId) {
        String sql = """
            SELECT COUNT(*) FROM awarded_prizes
            WHERE prize_id = ? AND family_id = ?
            AND (user_id IS NULL OR user_id = ?)
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, prizeId, familyId, userId);
        return count != null && count > 0;
    }

    @Override
    public List<AwardedPrize> getAwardsByFamilyId(int familyId) {
        String sql = """
            SELECT ap.award_id,
                   ap.prize_id,
                   p.prize_name,
                   ap.user_id,
                   u.username,
                   ap.family_id,
                   ap.awarded_date
            FROM awarded_prizes ap
            JOIN prizes p ON ap.prize_id = p.prize_id
            LEFT JOIN users u ON ap.user_id = u.user_id
            WHERE ap.family_id = ?
            ORDER BY ap.awarded_date DESC;
        """;
    
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            AwardedPrize ap = new AwardedPrize();
            ap.setAwardId(rs.getInt("award_id"));
            ap.setPrizeId(rs.getInt("prize_id"));
            ap.setPrizeName(rs.getString("prize_name"));      // New field
            ap.setUserId((Integer) rs.getObject("user_id"));
            ap.setUsername(rs.getString("username"));          // New field
            ap.setFamilyId(rs.getInt("family_id"));
            ap.setAwardedDate(rs.getTimestamp("awarded_date").toLocalDateTime());
            return ap;
        }, familyId);
    }
}
