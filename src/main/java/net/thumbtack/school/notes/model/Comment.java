package net.thumbtack.school.notes.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Comment {
    private int id;
    private LocalDateTime created;
    private String body;
    private User author;
    private NoteRevision noteRevision;
    
    
    public Comment(int id, LocalDateTime created, String body, User author, NoteRevision noteRevision) {
        setId(id);
        setCreated(created);
        setBody(body);
        setAuthor(author);
        setNoteRevision(noteRevision);
    }
    
    
    public Comment(LocalDateTime created, String body, User author, NoteRevision noteRevision) {
        this(0, created, body, author, noteRevision);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    
    public void setBody(String body) {
        this.body = body;
    }
    
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    
    public void setNoteRevision(NoteRevision noteRevision) {
        this.noteRevision = noteRevision;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public LocalDateTime getCreated() {
        return created;
    }
    
    
    public String getBody() {
        return body;
    }
    
    
    public User getAuthor() {
        return author;
    }
    
    
    public NoteRevision getNoteRevision() {
        return noteRevision;
    }
    
    
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", created=" + created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")) +
                ", author=" + author +
                ", revision=" + noteRevision +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getId() == comment.getId() &&
                Objects.equals(getCreated(), comment.getCreated()) &&
                Objects.equals(getBody(), comment.getBody());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreated(), getBody());
    }
}
