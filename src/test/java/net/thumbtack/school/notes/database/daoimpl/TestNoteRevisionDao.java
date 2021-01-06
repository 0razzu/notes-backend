package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class TestNoteRevisionDao extends TestDaoBase {
    private static final User author = new User("author", "Pa55word", "Author", null, "Author", UserType.USER);
    private static final Section section = new Section("section1", author);
    private static final Note note1 = new Note("subj1", author, LocalDateTime.of(2020, 12, 1, 0, 2, 3), section);
    private static final Note note2 = new Note("subj2", author, LocalDateTime.of(2020, 12, 1, 1, 3, 8), section);
    private static final NoteRevision revision11 = new NoteRevision("text1", LocalDateTime.of(2020, 12, 1, 0, 2, 3),
            note1);
    private static final NoteRevision revision12 = new NoteRevision("text1.2", LocalDateTime.of(2020, 12, 1, 0, 3, 0),
            note1);
    private static final NoteRevision revision21 = new NoteRevision("text2", LocalDateTime.of(2020, 12, 1, 0, 2, 3),
            note2);
    
    
    
    @Test
    void testGetByNote() throws ServerException {
        userDao.insert(author);
        sectionDao.insert(section);
        noteDao.insert(note1, revision11);
        noteDao.update(note1, revision12);
        noteDao.insert(note2, revision21);
        
        assertAll(
                () -> assertEquals(Set.of(revision11, revision12), Set.copyOf(noteRevisionDao.getByNote(note1))),
                () -> assertEquals(List.of(revision21), noteRevisionDao.getByNote(note2))
        );
    }
    
    
    @Test
    void testGetMostRecent() throws ServerException {
        userDao.insert(author);
        sectionDao.insert(section);
        noteDao.insert(note1, revision11);
        noteDao.update(note1, revision12);
        noteDao.insert(note2, revision21);
        
        assertAll(
                () -> assertEquals(revision12, noteRevisionDao.getMostRecent(note1)),
                () -> assertEquals(revision21, noteRevisionDao.getMostRecent(note2))
        );
    }
}
