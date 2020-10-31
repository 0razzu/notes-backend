package net.thumbtack.school.notes.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class Revision {
    private int id;
    private String body;
    private LocalDateTime created;
    private List<Comment> comments;
    
    
    public Revision() {
    }
    
    
    public Revision(int id, String body, LocalDateTime created, List<Comment> comments) {
        setId(id);
        setBody(body);
        setCreated(created);
    }
    
    
    public Revision(String body, LocalDateTime created, List<Comment> comments) {
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
        if (!(o instanceof Revision)) return false;
        Revision revision = (Revision) o;
        return getId() == revision.getId() &&
                Objects.equals(getBody(), revision.getBody()) &&
                Objects.equals(getCreated(), revision.getCreated()) &&
                Objects.equals(getComments(), revision.getComments());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody(), getCreated(), getComments());
    }
}
