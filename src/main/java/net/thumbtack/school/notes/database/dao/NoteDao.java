package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.view.NoteView;

import java.time.LocalDateTime;
import java.util.List;


public interface NoteDao {
    void insert(Note note, NoteRevision revision) throws ServerException;
    void update(Note note, NoteRevision revision) throws ServerException;
    
    Note get(int id) throws ServerException;
    NoteView getView(int id) throws ServerException;
    List<NoteView> getAllByParams(Integer sectionId, String sortByRating, String tags,
                                  LocalDateTime timeFrom, LocalDateTime timeTo,
                                  Integer author, int user, String include,
                                  boolean comments, boolean allVersions, boolean commentVersion,
                                  Integer from, Integer count) throws ServerException;
    
    void delete(Note note) throws ServerException;
}
