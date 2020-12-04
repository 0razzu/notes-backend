package net.thumbtack.school.notes.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Section {
    private int id;
    private String name;
    private User creator;
    private List<Note> notes;
    
    
    public Section(String name, User creator, List<Note> notes) {
        this(0, name, creator, notes);
    }
    
    
    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator=" + creator +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return getId() == section.getId() &&
                Objects.equals(getName(), section.getName());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
