package net.thumbtack.school.notes.view;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
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
    private boolean isSuper;
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
        return getId() == userView.getId() &&
                isOnline() == userView.isOnline() &&
                isDeleted() == userView.isDeleted() &&
                isSuper() == userView.isSuper() &&
                Double.compare(userView.getRating(), getRating()) == 0 &&
                Objects.equals(getFirstName(), userView.getFirstName()) &&
                Objects.equals(getPatronymic(), userView.getPatronymic()) &&
                Objects.equals(getLastName(), userView.getLastName()) &&
                Objects.equals(getLogin(), userView.getLogin()) &&
                Objects.equals(getTimeRegistered(), userView.getTimeRegistered());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getPatronymic(), getLastName(),
                getLogin(), getTimeRegistered(), isOnline(), isDeleted(), isSuper, getRating());
    }
}
