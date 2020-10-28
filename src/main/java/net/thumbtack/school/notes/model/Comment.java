package net.thumbtack.school.notes.model;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Comment {
    private Integer id;
    private ZonedDateTime created;
    private String body;
    private User author;
    private Note note;
    private Revision revision;
    
    
    public Comment(Integer id, ZonedDateTime created, String body, User author, Note note, Revision revision) {
        setId(id);
        setCreated(created);
        setBody(body);
        setAuthor(author);
        setNote(note);
        setRevision(revision);
    }
    
    
    public Comment(ZonedDateTime created, String body, User author, Note note, Revision revision) {
        this(null, created, body, author, note, revision);
    }
    
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public void setCreated(ZonedDateTime created) {
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
    
    
    public Integer getId() {
        return id;
    }
    
    
    public ZonedDateTime getCreated() {
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
        return Objects.equals(id, comment.id) &&
                Objects.equals(created, comment.created) &&
                Objects.equals(body, comment.body) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(note, comment.note) &&
                Objects.equals(revision, comment.revision);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, created, body, author, note, revision);
    }
}
