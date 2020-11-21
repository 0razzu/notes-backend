package net.thumbtack.school.notes.model;


import java.util.List;
import java.util.Objects;


public class Section {
    private int id;
    private String name;
    private User creator;
    private List<Note> notes;
    
    
    public Section() {
    }
    
    
    public Section(int id, String name, User creator, List<Note> notes) {
        setId(id);
        setName(name);
        setCreator(creator);
        setNotes(notes);
    }
    
    
    public Section(String name, User creator, List<Note> notes) {
        this(0, name, creator, notes);
    }
    
    
    public void setId(int id) {
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
    
    
    public int getId() {
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
        return getId() == section.getId() &&
                Objects.equals(getName(), section.getName());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
