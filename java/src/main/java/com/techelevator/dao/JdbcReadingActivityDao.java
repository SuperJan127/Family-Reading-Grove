/**
 * JDBC implementation of the ReadingActivityDao interface.
 * Handles CRUD operations for ReadingActivity records in the database.
 */
package com.techelevator.dao;

import com.techelevator.model.Format;
import com.techelevator.model.ReadingActivity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcReadingActivityDao implements ReadingActivityDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for JdbcReadingActivityDao.
     * @param jdbcTemplate the JdbcTemplate used to interact with the database.
     */
    public JdbcReadingActivityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts a new ReadingActivity row into the reading_activity table.
     * Sets reader_id, book_id, format, and notes.
     * Uses a KeyHolder to capture the generated primary key (id).
     * Then reloads the record to return a fully populated object.
     *
     * @param readingActivity the ReadingActivity object containing data to insert.
     * @return the newly created ReadingActivity, including generated id and timestamps.
     */
    @Override
    public ReadingActivity create(ReadingActivity readingActivity) {
        final String sql =
            "INSERT INTO reading_activity (reader_id, book_id, format, notes) " +
            "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, readingActivity.getReaderId());
            ps.setLong(2, readingActivity.getBookId());
            ps.setString(3, readingActivity.getFormat().name());
            ps.setString(4, readingActivity.getNotes());
            return ps;
        }, keyHolder);

        long newId = keyHolder.getKey().longValue();
        return getById(newId);
    }

    /**
     * Retrieves a ReadingActivity record by its id.
     *
     * @param id the id of the ReadingActivity to retrieve.
     * @return the ReadingActivity object if found, or null if not found.
     */
    @Override
    public ReadingActivity getById(long id) {
        final String sql = "SELECT * FROM reading_activity WHERE id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            return mapRowToReadingActivity(rs);
        }
        return null;
    }

    /**
     * Retrieves all ReadingActivity records for a given reader, ordered by start_time descending.
     *
     * @param readerId the id of the reader/user.
     * @return a list of ReadingActivity objects.
     */
    @Override
    public List<ReadingActivity> getByReaderId(long readerId) {
        final String sql =
            "SELECT * FROM reading_activity " +
            "WHERE reader_id = ? " +
            "ORDER BY start_time DESC";
        List<ReadingActivity> list = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, readerId);
        while (rs.next()) {
            list.add(mapRowToReadingActivity(rs));
        }
        return list;
    }

    /**
     * Retrieves all open (in-flight) ReadingActivity sessions for a reader
     * where end_time is null.
     *
     * @param readerId the id of the reader/user.
     * @return a list of ReadingActivity objects that have not been ended.
     */
    @Override
    public List<ReadingActivity> getOpenSessionsByReaderId(long readerId) {
        final String sql =
            "SELECT * FROM reading_activity " +
            "WHERE reader_id = ? AND end_time IS NULL " +
            "ORDER BY start_time DESC";
        List<ReadingActivity> list = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, readerId);
        while (rs.next()) {
            list.add(mapRowToReadingActivity(rs));
        }
        return list;
    }

    /**
     * Updates an existing ReadingActivity record to set end_time and minutes_read.
     * Returns the updated ReadingActivity object.
     *
     * @param readingActivity the ReadingActivity containing updated endTime and minutesRead.
     * @return the updated ReadingActivity object.
     */
    @Override
    public ReadingActivity update(ReadingActivity readingActivity) {
        final String sql =
            "UPDATE reading_activity SET end_time = ?, minutes_read = ? " +
            "WHERE id = ?";
        jdbcTemplate.update(sql,
            Timestamp.valueOf(readingActivity.getEndTime()),
            readingActivity.getMinutesRead(),
            readingActivity.getId()
        );
        return getById(readingActivity.getId());
    }

    /**
     * Maps a row from SqlRowSet to a ReadingActivity object.
     *
     * @param rs the SqlRowSet positioned at the current row.
     * @return a ReadingActivity object populated with data from the current row.
     */
    private ReadingActivity mapRowToReadingActivity(SqlRowSet rs) {
        ReadingActivity a = new ReadingActivity();
        a.setId(rs.getLong("id"));
        a.setReaderId(rs.getLong("reader_id"));
        a.setBookId(rs.getLong("book_id"));
        a.setFormat(Format.valueOf(rs.getString("format")));
        a.setNotes(rs.getString("notes"));
        Timestamp startTs = rs.getTimestamp("start_time");
        if (startTs != null) {
            a.setStartTime(startTs.toLocalDateTime());
        }
        Timestamp endTs = rs.getTimestamp("end_time");
        if (endTs != null) {
            a.setEndTime(endTs.toLocalDateTime());
        }
        int mins = rs.getInt("minutes_read");
        if (!rs.wasNull()) {
            a.setMinutesRead(mins);
        }
        return a;
    }
}