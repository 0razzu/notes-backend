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
public class UserView {
    private int id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String login;
    private LocalDateTime timeRegistered;
    private boolean isOnline;
    private boolean isDeleted;
    private Boolean isSuper;
    private double rating;
    
    
    @Override
    public String toString() {
        return "UserView{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", isOnline=" + isOnline +
                ", isDeleted=" + isDeleted +
                ", isSuper=" + isSuper +
                ", rating=" + rating +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserView)) return false;
        UserView userView = (UserView) o;
        return getId() == userView.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
