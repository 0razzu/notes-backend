package net.thumbtack.school.notes.view;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.notes.util.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommentView {
    private int id;
    private String body;
    private int authorId;
    private Integer noteRevisionId;
    private LocalDateTime created;
    
    
    @Override
    public String toString() {
        return "CommentView{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", noteRevisionId=" + noteRevisionId +
                ", created=" + LocalDateTimeFormatter.format(created) +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentView)) return false;
        CommentView that = (CommentView) o;
        return getId() == that.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
