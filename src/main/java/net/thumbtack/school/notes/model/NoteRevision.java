package net.thumbtack.school.notes.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class NoteRevision {
    private int id;
    private String body;
    private LocalDateTime created;
    private List<Comment> comments;
    
    
    public NoteRevision() {
    }
    
    
    public NoteRevision(int id, String body, LocalDateTime created, List<Comment> comments) {
        setId(id);
        setBody(body);
        setCreated(created);
    }
    
    
    public NoteRevision(String body, LocalDateTime created, List<Comment> comments) {
        this(0, body, created, comments);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setBody(String body) {
        this.body = body;
    }
    
    
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public String getBody() {
        return body;
    }
    
    
    public LocalDateTime getCreated() {
        return created;
    }
    
    
    public List<Comment> getComments() {
        return comments;
    }
    
    
    @Override
    public String toString() {
        return "Revision{" +
                "id=" + id +
                ", created=" + created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")) +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteRevision)) return false;
        NoteRevision noteRevision = (NoteRevision) o;
        return getId() == noteRevision.getId() &&
                Objects.equals(getBody(), noteRevision.getBody()) &&
                Objects.equals(getCreated(), noteRevision.getCreated()) &&
                Objects.equals(getComments(), noteRevision.getComments());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody(), getCreated(), getComments());
    }
}
