package com.example.a2340project.model;

public class Note {
    private String noteId;
    private String authorId;
    private String content;
    private long timestamp;

    // Default constructor for Firebase
    public Note() {}

    public Note(String noteId, String authorId, String content, long timestamp) {
        this.noteId = noteId;
        this.authorId = authorId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getNoteId() { return noteId; }
    public void setNoteId(String noteId) { this.noteId = noteId; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
