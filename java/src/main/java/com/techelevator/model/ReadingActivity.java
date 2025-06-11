package com.techelevator.model;

public class ReadingActivity {

    private long id;
    private long readerId;
    private long bookId;
    private Format format;
    private int minutes;
    private String notes;

    // getters & setters

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getReaderId() { return readerId; }
    public void setReaderId(long readerId) { this.readerId = readerId; }

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public Format getFormat() { return format; }
    public void setFormat(Format format) { this.format = format; }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
