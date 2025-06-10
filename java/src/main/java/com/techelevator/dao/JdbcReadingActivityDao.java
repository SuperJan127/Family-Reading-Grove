package com.techelevator.dao;

import com.techelevator.model.Format;
import com.techelevator.model.ReadingActivity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
            + "INSERT INTO reading_activity (reader_id, book_id, format, minutes, notes) "
            + "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            activity.getReaderId(),
            activity.getBookId(),
            activity.getFormat().name(),
            activity.getMinutes(),
            activity.getNotes()
        );
    }

    /**
     * Fetch the reading history (all sessions) for a given user.
     */
    @Override
    public List<ReadingActivity> getReadingHistory(long readerId) {
        final String sql = ""
            + "SELECT id, reader_id, book_id, format, minutes, notes "
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
                return act;
            },
            readerId
        );
    }
}

