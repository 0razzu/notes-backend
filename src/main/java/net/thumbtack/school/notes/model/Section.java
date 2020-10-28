package net.thumbtack.school.notes.model;


import java.util.List;
import java.util.Objects;


public class Section {
    private Integer id;
    private String name;
    private User creator;
    private List<Note> notes;
    
    
    public Section() {
    }
    
    
    public Section(Integer id, String name, User creator, List<Note> notes) {
        setId(id);
        setName(name);
        setCreator(creator);
        setNotes(notes);
    }
    
    
    public Section(String name, User creator, List<Note> notes) {
        this(null, name, creator, notes);
    }
    
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    
    public Integer getId() {
        return id;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public User getCreator() {
        return creator;
    }
    
    
    public List<Note> getNotes() {
        return notes;
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
        return Objects.equals(id, section.id) &&
                Objects.equals(name, section.name) &&
                Objects.equals(creator, section.creator) &&
                Objects.equals(notes, section.notes);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator, notes);
    }
}
