package net.thumbtack.school.notes.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Setter
@Getter
public class User {
    private int id;
    private String login;
    private String password;
    private String firstName;
    private String patronymic;
    private String lastName;
    private UserType type;
    private boolean deleted;
    private List<Note> notes;
    private List<Comment> comments;
    private List<Rating> ratings;
    private List<Section> sections;
    private List<User> following;
    private List<User> followers;
    private List<User> ignore;
    private List<User> ignoredBy;
    
    
    public User(int id, String login, String password, String firstName, String patronymic, String lastName,
                UserType type) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setFirstName(firstName);
        setPatronymic(patronymic);
        setLastName(lastName);
        setType(type);
    }
    
    
    public User(String login, String password, String firstName, String patronymic, String lastName, UserType type) {
        this(0, login, password, firstName, patronymic, lastName, type);
    }
    
    
    public User(String login, String password, String firstName, String patronymic, String lastName,
                UserType type, boolean deleted) {
        this(login, password, firstName, patronymic, lastName, type);
        setDeleted(deleted);
    }
    
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", type=" + type +
                ", deleted=" + deleted +
                '}';
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getPatronymic(), user.getPatronymic()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                getType() == user.getType();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getPassword(), getFirstName(), getPatronymic(), getLastName(), getType());
    }
}
