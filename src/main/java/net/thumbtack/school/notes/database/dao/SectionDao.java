package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;

import java.util.List;


public interface SectionDao {
    void insert(Section section) throws ServerException;
    void update(Section section) throws ServerException;
    
    Section get(int id) throws ServerException;
    List<Section> getByCreator(User creator) throws ServerException;
    List<Section> getAll() throws ServerException;
    
    void delete(Section section) throws ServerException;
}
