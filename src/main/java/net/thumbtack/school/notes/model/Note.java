package net.thumbtack.school.notes.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.notes.util.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Note {
    private int id;
    private String subject;
    private User author;
    private LocalDateTime created;
    private Section section;
    private List<NoteRevision> noteRevisions;
    private List<Rating> ratings;
    
    
    public Note(String subject, User author, LocalDateTime created, Section section) {
        this(0, subject, author, null, section, null, null);
    }
    
    
    @Override
    public String toString() {
        return String.format("Note{id=%d, subject='%s', created=%s}", id, subject,
                created == null? "null" : LocalDateTimeFormatter.format(created));
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return getId() == note.getId() &&
                Objects.equals(getSubject(), note.getSubject()) &&
                Objects.equals(getCreated(), note.getCreated());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSubject(), getCreated());
    }
}
