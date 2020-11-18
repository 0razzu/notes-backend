package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.model.User;

import java.util.List;


public interface SessionDao {
    void insert(User user, String token);
    
    User getUserByToken(String token);
    boolean isOnline(User user);
    boolean isOnline(String token);
    
    List<User> getOnline();
    
    void delete(User user);
    void delete(String token);
}
