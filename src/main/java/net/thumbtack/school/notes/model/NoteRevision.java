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
    private String body;
    private LocalDateTime created;
    private Note note;
    private List<Comment> comments;
    
    
    public NoteRevision(String body, LocalDateTime created, Note note, List<Comment> comments) {
        this(0, body, created, note, comments);
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
