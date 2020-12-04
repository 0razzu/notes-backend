package net.thumbtack.school.notes.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Comment {
    private int id;
    private LocalDateTime created;
    private String body;
    private User author;
    private NoteRevision noteRevision;
    
    
    public Comment(LocalDateTime created, String body, User author, NoteRevision noteRevision) {
        this(0, created, body, author, noteRevision);
    }
    
    
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", created=" + created.format(DateTimeFormatter.ofPattern("''yyyy.MM.dd HH:mm:ss''")) +
                ", author=" + author +
                ", revision=" + noteRevision +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getId() == comment.getId() &&
                Objects.equals(getCreated(), comment.getCreated()) &&
                Objects.equals(getBody(), comment.getBody());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreated(), getBody());
    }
}
