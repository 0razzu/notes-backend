package net.thumbtack.school.notes.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NoteRevision {
    private int id;
    private String subject;
    private String body;
    private LocalDateTime created;
    private Note note;
    private List<Comment> comments;
    
    
    public NoteRevision(String subject, String body, LocalDateTime created, Note note) {
        this(0, subject, body, created, note, null);
    }
    
    
    @Override
    public String toString() {
        return String.format("NoteRevision{id=%d, subject=%s, created=%s}", id, subject,
                created == null? "null" : created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")));
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteRevision)) return false;
        NoteRevision revision = (NoteRevision) o;
        return getId() == revision.getId() &&
                Objects.equals(getSubject(), revision.getSubject()) &&
                Objects.equals(getBody(), revision.getBody()) &&
                Objects.equals(getCreated(), revision.getCreated());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSubject(), getBody(), getCreated());
    }
}
