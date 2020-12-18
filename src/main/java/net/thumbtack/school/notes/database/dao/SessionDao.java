package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;

import java.util.List;


public interface SessionDao {
    void insert(User user, String token) throws ServerException;
    
    User getUserByToken(String token) throws ServerException;
    boolean isOnline(User user) throws ServerException;
    boolean isOnline(String token) throws ServerException;
    
    List<User> getOnline() throws ServerException;
    
    void delete(User user) throws ServerException;
    void delete(String token) throws ServerException;
    void deleteAll() throws ServerException;
}
