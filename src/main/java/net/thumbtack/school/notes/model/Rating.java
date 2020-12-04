package net.thumbtack.school.notes.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rating {
    private int id;
    private int value;
    private User author;
    
    
    public Rating(int value, User author) {
        this(0, value, author);
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
