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
public class Note {
    private int id;
    private String subject;
    private User author;
    private LocalDateTime created;
    private List<NoteRevision> noteRevisions;
    private List<Rating> ratings;
    private Section section;
    
    
    public Note(String subject, User author, LocalDateTime created, Section section) {
        this(0, subject, author, null, null, null, section);
    }
    
    
    @Override
    public String toString() {
        return String.format("Note{id=%d, subject='%s', created=%s}", id, subject,
                created == null? "null" : created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")));
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
