package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;

import java.util.List;


public interface NoteRevisionDao {
    List<NoteRevision> getByNote(Note note) throws ServerException;
    NoteRevision getMostRecent(Note note) throws ServerException;
}
