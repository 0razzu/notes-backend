package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestRatingDao extends TestDaoBase {
    @Test
    void testInsert() throws ServerException {
        User ann = new User("ann", "annann123", "Ann", null, "White", UserType.USER);
        Section section = new Section("s1", ann);
        Note note = new Note(ann, LocalDateTime.of(2021, 1, 1, 2, 9), section);
        NoteRevision revision = new NoteRevision("n1", "some text", LocalDateTime.of(2021, 1, 1, 3, 0), note);
        Rating rating = new Rating(5, admin);
        
        userDao.insert(ann);
        sectionDao.insert(section);
        noteDao.insert(note, revision);
        
        ratingDao.insert(rating, note);
        
        assertEquals(List.of(rating), noteDao.get(note.getId()).getRatings());
    }
}
