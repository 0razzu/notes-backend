package net.thumbtack.school.notes.view;


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
public class NoteRevisionView {
    private int id;
    private String body;
    private LocalDateTime created;
    private List<CommentView> comments;
    
    
    @Override
    public String toString() {
        return "NoteRevisionView{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", created=" + LocalDateTimeFormatter.format(created) +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteRevisionView)) return false;
        NoteRevisionView that = (NoteRevisionView) o;
        return getId() == that.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
