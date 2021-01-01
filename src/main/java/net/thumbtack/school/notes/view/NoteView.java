package net.thumbtack.school.notes.view;


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
public class NoteView {
    private int id;
    private String subject;
    private String body;
    private int sectionId;
    private int authorId;
    private LocalDateTime created;
    private int revisionId;
    
    
    @Override
    public String toString() {
        return "NoteView{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", sectionId=" + sectionId +
                ", authorId=" + authorId +
                ", created=" + created +
                ", revisionId=" + revisionId +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteView)) return false;
        NoteView noteView = (NoteView) o;
        return getId() == noteView.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
