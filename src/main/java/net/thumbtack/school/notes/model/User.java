package net.thumbtack.school.notes.model;


import java.util.List;
import java.util.Objects;


public class User {
    private int id;
    private String login;
    private String password;
    private String firstName;
    private String patronymic;
    private String lastName;
    private Role role;
    private List<Note> notes;
    private List<Comment> comments;
    private List<Rating> ratings;
    private List<Section> sections;
    
    
    public User() {
    }
    
    
    public User(int id, String login, String password,
                String firstName, String patronymic, String lastName, Role role,
                List<Note> notes, List<Comment> comments, List<Rating> ratings, List<Section> sections) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setFirstName(firstName);
        setPatronymic(patronymic);
        setLastName(lastName);
        setRole(role);
        setNotes(notes);
        setComments(comments);
        setRatings(ratings);
        setSections(sections);
    }
    
    
    public User(String login, String password,
                String firstName, String patronymic, String lastName, Role role,
                List<Note> notes, List<Comment> comments, List<Rating> ratings, List<Section> sections) {
        this(0, login, password, firstName, patronymic, lastName, role, notes, comments, ratings, sections);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    
    public void setPassword(String password) {
        this.password = password;
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
    
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    
    
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public String getLogin() {
        return login;
    }
    
    
    public String getPassword() {
        return password;
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
    
    
    public Role getRole() {
        return role;
    }
    
    
    public List<Note> getNotes() {
        return notes;
    }
    
    
    public List<Comment> getComments() {
        return comments;
    }
    
    
    public List<Rating> getRatings() {
        return ratings;
    }
    
    
    public List<Section> getSections() {
        return sections;
    }
    
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login=" + login +
                ", role=" + role +
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
                getRole() == user.getRole() &&
                Objects.equals(getNotes(), user.getNotes()) &&
                Objects.equals(getComments(), user.getComments()) &&
                Objects.equals(getRatings(), user.getRatings()) &&
                Objects.equals(getSections(), user.getSections());
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getPassword(), getFirstName(), getPatronymic(), getLastName(), getRole(), getNotes(), getComments(), getRatings(), getSections());
    }
}
