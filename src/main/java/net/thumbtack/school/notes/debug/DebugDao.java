package net.thumbtack.school.notes.debug;


import net.thumbtack.school.notes.error.ServerException;


public interface DebugDao {
    void clear() throws ServerException;
}
