package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;


public interface CommonDao {
    void clear() throws ServerException;
}
