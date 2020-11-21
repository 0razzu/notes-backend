package net.thumbtack.school.notes.view;


import java.time.LocalDateTime;
import java.util.Objects;


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
    
    
    public UserView() {
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    
    public void setTimeRegistered(LocalDateTime timeRegistered) {
        this.timeRegistered = timeRegistered;
    }
    
    
    public void setOnline(boolean online) {
        isOnline = online;
    }
    
    
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    
    
    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }
    
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public String getFirstName() {
        return firstName;
    }
    
    
    public String getPatronymic() {
        return patronymic;
    }
    
    
    public String getLastName() {
        return lastName;
    }
    
    
    public String getLogin() {
        return login;
    }
    
    
    public LocalDateTime getTimeRegistered() {
        return timeRegistered;
    }
    
    
    public boolean isOnline() {
        return isOnline;
    }
    
    
    public boolean isDeleted() {
        return isDeleted;
    }
    
    
    public boolean isSuper() {
        return isSuper;
    }
    
    
    public double getRating() {
        return rating;
    }
    
    
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
