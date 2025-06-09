package com.techelevator.model;

import java.time.LocalDateTime;

public class ReadingActivity {

    private long id;
    private long readerId;
    private long bookId;
    private Format format;
    private String notes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer minutesRead;

    public ReadingActivity() { }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getReaderId() {
        return readerId;
    }
    public void setReaderId(long readerId) {
        this.readerId = readerId;
    }
    public long getBookId() {
        return bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public Format getFormat() {
        return format;
    }
    public void setFormat(Format format) {
        this.format = format;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Integer getMinutesRead() {
        return minutesRead;
    }
    public void setMinutesRead(Integer minutesRead) {
        this.minutesRead = minutesRead;
    }
}

