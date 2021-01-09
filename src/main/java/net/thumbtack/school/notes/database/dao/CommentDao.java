package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;

import java.util.List;


public interface CommentDao {
    void insert(Comment comment) throws ServerException;
    void update(Comment comment) throws ServerException;
    
    Comment get(int id) throws ServerException;
    List<Comment> getByAuthor(User author) throws ServerException;
    List<Comment> getByNoteRevision(NoteRevision revision) throws ServerException;
    List<Comment> getByNote(Note note) throws ServerException;
    
    void delete(Comment comment) throws ServerException;
    void deleteByMostRecentNoteRevision(Note note) throws ServerException;
}
