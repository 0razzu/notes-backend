package net.thumbtack.school.notes.model;


import java.util.Objects;


public class Rating {
    private int id;
    private int value;
    private User author;
    
    
    public Rating() {
    }
    
    
    public Rating(int id, int value, User author) {
        setId(id);
        setValue(value);
        setAuthor(author);
    }
    
    
    public Rating(int value, User author) {
        this(0, value, author);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setValue(int value) {
        this.value = value;
    }
    
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public int getValue() {
        return value;
    }
    
    
    public User getAuthor() {
        return author;
    }
    
    
    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", value=" + value +
                ", author=" + author +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return getId() == rating.getId() &&
                getValue() == rating.getValue();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getValue());
    }
}
