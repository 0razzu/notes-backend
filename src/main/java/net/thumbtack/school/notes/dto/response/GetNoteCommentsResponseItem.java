package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetNoteCommentsResponseItem {
    private int id;
    private String body;
    private int noteId;
    private int authorId;
    private int revisionId;
    private LocalDateTime created;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetNoteCommentsResponseItem)) return false;
        GetNoteCommentsResponseItem that = (GetNoteCommentsResponseItem) o;
        return getId() == that.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
