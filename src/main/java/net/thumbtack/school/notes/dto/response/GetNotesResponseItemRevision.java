package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetNotesResponseItemRevision {
    private int id;
    private String body;
    private LocalDateTime created;
    private List<GetNotesResponseItemComment> comments;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetNotesResponseItemRevision)) return false;
        GetNotesResponseItemRevision that = (GetNotesResponseItemRevision) o;
        return getId() == that.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
