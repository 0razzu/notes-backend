package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.view.NoteView;


public interface NoteDao {
    void insert(Note note, NoteRevision revision) throws ServerException;
    void update(Note note, NoteRevision revision) throws ServerException;
    
    Note get(int id) throws ServerException;
    NoteView getView(int id) throws ServerException;
    
    void delete(Note note) throws ServerException;
}
