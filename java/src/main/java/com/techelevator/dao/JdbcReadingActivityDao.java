package com.techelevator.dao;

import com.techelevator.model.Format;
import com.techelevator.model.ReadingActivity;
import com.techelevator.model.UserMinutesDTO;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Repository
public class JdbcReadingActivityDao implements ReadingActivityDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReadingActivityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Persist a new reading session (minutes read) for a user.
     */
    @Override
    public void recordReadingTime(ReadingActivity activity) {
        final String sql = ""
                + "INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes, date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                activity.getReaderId(),
                activity.getBookId(),
                activity.getFormat().name(),
                activity.getMinutes(),
                activity.getNotes(),
                activity.getDate() != null ? java.sql.Date.valueOf(activity.getDate()) : null); // convert LocalDate to
                                                                                                // SQL Date
    }

    /**
     * Fetch the reading history (all sessions) for a given user.
     */
    @Override
    public List<ReadingActivity> getReadingHistory(long readerId) {
        final String sql = ""
                + "SELECT id, reader_id, book_id, format, minutes, notes, date "
                + "FROM reading_activity "
                + "WHERE reader_id = ? "
                + "ORDER BY id";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    ReadingActivity act = new ReadingActivity();
                    act.setId(rs.getLong("id"));
                    act.setReaderId(rs.getLong("reader_id"));
                    act.setBookId(rs.getLong("book_id"));
                    act.setFormat(Format.valueOf(rs.getString("format")));
                    act.setMinutes(rs.getInt("minutes"));
                    act.setNotes(rs.getString("notes"));
                    act.setDate(rs.getDate("date").toLocalDate()); // convert SQL date to LocalDate
                    return act;
                },
                readerId);
    }

    /**
     * Fetch all reading activities for every member of a given family.
     */
    @Override
    public List<ReadingActivity> getActivitiesByFamilyId(int familyId) {
        final String sql = ""
                + "SELECT ra.id, "
                + "       ra.reader_id, "
                + "       u.username AS reader_username, "
                + "       ra.book_id, "
                + "       b.title    AS book_title, "
                + "       b.author   AS book_author, "
                + "       b.isbn     AS book_isbn, "
                + "       ra.format, "
                + "       ra.minutes, "
                + "       ra.notes, "
                + "       ra.date "
                + "  FROM reading_activity ra "
                + "  JOIN users u ON ra.reader_id = u.user_id "
                + "  JOIN books b ON ra.book_id    = b.book_id "
                + " WHERE u.family_id = ? "
                + " ORDER BY ra.id DESC"; // newest first; drop DESC if we want oldest first

        return jdbcTemplate.query(
                sql,
                mapRowToReadingActivityWithDetails(),
                familyId);
    }

    /**
     * Calculate the total minutes read by a specific user.
     */
    @Override
    public int getTotalMinutesByUserId(int userId) {
        final String sql = "SELECT COALESCE(SUM(minutes), 0) FROM reading_activity WHERE reader_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    /**
     * Calculate the total minutes read by all users in a family.
     */
    @Override
    public int getTotalMinutesByFamilyId(int familyId) {
        final String sql = """
                    SELECT COALESCE(SUM(ra.minutes), 0)
                    FROM reading_activity ra
                    JOIN users u ON ra.reader_id = u.id
                    WHERE u.family_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, familyId);
    }

    @Override
public List<UserMinutesDTO> getTotalMinutesByUserInFamily(int familyId) {
    final String sql = """
        SELECT 
            u.user_id,
            u.username,
           
            COALESCE(SUM(ra.minutes), 0) AS total_minutes
        FROM users u
        LEFT JOIN reading_activity ra ON u.user_id = ra.reader_id
        WHERE u.family_id = ?
        GROUP BY u.user_id, u.username 
        ORDER BY u.username
    """;

    return jdbcTemplate.query(sql, (rs, rowNum) -> {
        UserMinutesDTO dto = new UserMinutesDTO();
        dto.setUserId(rs.getInt("user_id"));
        dto.setUsername(rs.getString("username"));
    
        dto.setTotalMinutes(rs.getInt("total_minutes"));
        return dto;
    }, familyId);
}

    /**
     * RowMapper for simple reading activity.
     */
    private RowMapper<ReadingActivity> mapRowToReadingActivity() {
        return (rs, rowNum) -> {
            ReadingActivity ra = new ReadingActivity();
            ra.setId(rs.getLong("id"));
            ra.setReaderId(rs.getLong("reader_id"));
            ra.setBookId(rs.getLong("book_id"));
            ra.setFormat(Format.valueOf(rs.getString("format")));
            ra.setMinutes(rs.getInt("minutes"));
            ra.setNotes(rs.getString("notes"));
            ra.setDate(rs.getDate("date").toLocalDate()); // convert SQL date to LocalDate

            return ra;
        };
    }

    /**
     * RowMapper for family-wide reading activity with details.
     */
    private RowMapper<ReadingActivity> mapRowToReadingActivityWithDetails() {
        return (rs, rowNum) -> {
            ReadingActivity ra = new ReadingActivity();
            ra.setId(rs.getLong("id"));
            ra.setReaderId(rs.getLong("reader_id"));
            ra.setUsername(rs.getString("reader_username"));
            ra.setBookId(rs.getLong("book_id"));
            ra.setTitle(rs.getString("book_title"));
            ra.setAuthor(rs.getString("book_author"));
            ra.setIsbn(rs.getString("book_isbn"));
            ra.setFormat(Format.valueOf(rs.getString("format")));
            ra.setMinutes(rs.getInt("minutes"));
            ra.setNotes(rs.getString("notes"));
            ra.setDate(rs.getDate("date").toLocalDate()); // convert SQL date to LocalDate

            return ra;
        };
    }
}
