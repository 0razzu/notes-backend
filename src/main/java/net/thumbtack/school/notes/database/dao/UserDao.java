package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.model.User;


public interface UserDao {
    void insert(User user);
    void update(User user);
    User get(int id);
}
