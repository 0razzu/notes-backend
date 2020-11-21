package net.thumbtack.school.notes.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class NoteRevision {
    private int id;
    private String body;
    private LocalDateTime created;
    private Note note;
    private List<Comment> comments;
    
    
    public NoteRevision() {
    }
    
    
    public NoteRevision(int id, String body, LocalDateTime created, Note note, List<Comment> comments) {
        setId(id);
        setBody(body);
        setCreated(created);
        setNote(note);
        setComments(comments);
    }
    
    
    public NoteRevision(String body, LocalDateTime created, Note note, List<Comment> comments) {
        this(0, body, created, note, comments);
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
    
    
    public void setNote(Note note) {
        this.note = note;
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
    
    
    public Note getNote() {
        return note;
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
        NoteRevision that = (NoteRevision) o;
        return getId() == that.getId() &&
                Objects.equals(getBody(), that.getBody());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody());
    }
}
