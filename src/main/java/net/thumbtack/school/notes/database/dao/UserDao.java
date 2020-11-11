package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.model.User;

import java.util.List;


public interface UserDao {
    void insert(User user);
    void update(User user);
    User get(int id);
    List<User> getAll();
}
