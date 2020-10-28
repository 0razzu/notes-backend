package net.thumbtack.school.notes.model;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class Revision {
    private Integer id;
    private String body;
    private ZonedDateTime created;
    private List<Comment> comments;
    
    
    public Revision() {
    }
    
    
    public Revision(Integer id, String body, ZonedDateTime created, List<Comment> comments) {
        setId(id);
        setBody(body);
        setCreated(created);
    }
    
    
    public Revision(String body, ZonedDateTime created, List<Comment> comments) {
        this(null, body, created, comments);
    }
    
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public void setBody(String body) {
        this.body = body;
    }
    
    
    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }
    
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    
    public Integer getId() {
        return id;
    }
    
    
    public String getBody() {
        return body;
    }
    
    
    public ZonedDateTime getCreated() {
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
        return Objects.equals(getId(), revision.getId()) &&
                Objects.equals(getBody(), revision.getBody()) &&
                Objects.equals(getCreated(), revision.getCreated()) &&
                Objects.equals(getComments(), revision.getComments());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody(), getCreated(), getComments());
    }
}
