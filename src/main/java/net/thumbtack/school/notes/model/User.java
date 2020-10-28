package net.thumbtack.school.notes.model;


import java.util.Objects;


public class User {
    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String patronymic;
    private String lastName;
    private Role role;
    
    
    public User() {
    }
    
    
    public User(Integer id, String login, String password, String firstName, String patronymic, String lastName, Role role) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setFirstName(firstName);
        setPatronymic(patronymic);
        setLastName(lastName);
        setRole(role);
    }
    
    
    public User(String login, String password, String firstName, String patronymic, String lastName, Role role) {
        this(null, login, password, firstName, patronymic, lastName, role);
    }
    
    
    public void setId(Integer id) {
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
    
    
    public Integer getId() {
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
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getPatronymic(), user.getPatronymic()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                getRole() == user.getRole();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getPassword(), getFirstName(), getPatronymic(), getLastName(), getRole());
    }
}
