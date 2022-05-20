package net.thumbtack.school.notes.view;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ShortUserView {
    private int id;
    private String login;
    private String firstName;
    private String patronymic;
    private String lastName;
    private Boolean isSuper;
    private boolean isFollowed;
    private boolean isIgnored;
    
    
    @Override
    public String toString() {
        return "ShortUserView{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isSuper=" + isSuper +
                ", isFollowed=" + isFollowed +
                ", isIgnored=" + isIgnored +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortUserView)) return false;
        ShortUserView that = (ShortUserView) o;
        return getId() == that.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
