package net.thumbtack.school.notes.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Comment {
    private int id;
    private LocalDateTime created;
    private String body;
    private User author;
    private Note note;
    private Revision revision;
    
    
    public Comment(int id, LocalDateTime created, String body, User author, Note note, Revision revision) {
        setId(id);
        setCreated(created);
        setBody(body);
        setAuthor(author);
        setNote(note);
        setRevision(revision);
    }
    
    
    public Comment(LocalDateTime created, String body, User author, Note note, Revision revision) {
        this(0, created, body, author, note, revision);
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
    
    
    public void setNote(Note note) {
        this.note = note;
    }
    
    
    public void setRevision(Revision revision) {
        this.revision = revision;
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
    
    
    public Note getNote() {
        return note;
    }
    
    
    public Revision getRevision() {
        return revision;
    }
    
    
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", created=" + created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")) +
                ", author=" + author +
                ", note=" + note +
                ", revision=" + revision +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getId() == comment.getId() &&
                Objects.equals(getCreated(), comment.getCreated()) &&
                Objects.equals(getBody(), comment.getBody()) &&
                Objects.equals(getAuthor(), comment.getAuthor()) &&
                Objects.equals(getNote(), comment.getNote()) &&
                Objects.equals(getRevision(), comment.getRevision());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreated(), getBody(), getAuthor(), getNote(), getRevision());
    }
}
