package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;


public interface SessionDao {
    void insert(User user, String token) throws ServerException;
    void update(String token) throws ServerException;
    
    User getUserByToken(String token) throws ServerException;
    
    void delete(User user) throws ServerException;
    void delete(String token) throws ServerException;
    void deleteOutdated();
    void deleteAll() throws ServerException;
}
