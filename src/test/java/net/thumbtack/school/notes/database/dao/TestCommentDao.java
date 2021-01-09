package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class TestCommentDao extends TestDaoBase {
    private static final User lia = new User("l1a", "1234qwer", "Lia", null, "Ro", UserType.SUPER);
    private static final User dan = new User("dan", "as;ldfr3", "Dan", null, "Witt", UserType.USER);
    private static final Section section1 = new Section("section 1", admin);
    private static final Section section2 = new Section("section 2", lia);
    private static final Note liaNote = new Note(lia, LocalDateTime.of(2021, 1, 1, 0, 0, 0), section1);
    private static final Note danNote1 = new Note(dan, LocalDateTime.of(2021, 1, 1, 0, 0, 0), section1);
    private static final Note danNote2 = new Note(dan, LocalDateTime.of(2021, 1, 1, 0, 2, 0), section2);
    private static final NoteRevision liaRev1 = new NoteRevision("Lia’s note", "nothing interesting", liaNote.getCreated(), liaNote);
    private static final NoteRevision liaRev2 = new NoteRevision("Lia’s note", "something interesting", liaNote.getCreated().plusHours(1), liaNote);
    private static final NoteRevision danRev1 = new NoteRevision("Dan’s note 1", "hello", danNote1.getCreated(), danNote1);
    private static final NoteRevision danRev2 = new NoteRevision("Dan’s note 2", "hello again", danNote2.getCreated(), danNote2);
    
    
    private boolean compareComments(Comment comment1, Comment comment2) {
        return comment1.equals(comment2) &&
                Objects.equals(comment1.getAuthor(), comment2.getAuthor()) &&
                Objects.equals(comment1.getNoteRevision(), comment2.getNoteRevision());
    }
    
    
    private boolean compareCommentLists(List<Comment> list1, List<Comment> list2, boolean sort) {
        if (list1.size() != list2.size())
            return false;
        
        if (sort) {
            list1.sort(Comparator.comparing(Comment::getId));
            list2.sort(Comparator.comparing(Comment::getId));
        }
        
        for (int i = 0; i < list1.size(); i++)
            if (!compareComments(list1.get(i), list2.get(i)))
                return false;
        
        return true;
    }
    
    
    @Test
    void testInsert() throws ServerException {
        userDao.insert(lia);
        sectionDao.insert(section1);
        noteDao.insert(liaNote, liaRev1);
        Comment comment = new Comment(liaNote.getCreated().plusSeconds(5), "smth", lia, liaRev1);
        
        commentDao.insert(comment);
        
        assertTrue(compareComments(comment, commentDao.get(comment.getId())));
    }
    
    
    @Test
    void testUpdate() throws ServerException {
        userDao.insert(lia);
        sectionDao.insert(section1);
        noteDao.insert(liaNote, liaRev1);
        noteDao.update(liaNote, liaRev2);
        Comment comment = new Comment(liaNote.getCreated().plusSeconds(5), "smth", lia, liaRev1);
        commentDao.insert(comment);
        
        comment.setCreated(comment.getCreated().plusDays(10));
        comment.setBody("smth new");
        comment.setNoteRevision(liaRev2);
        commentDao.update(comment);
        
        assertTrue(compareComments(comment, commentDao.get(comment.getId())));
    }
    
    
    @Test
    void testGetByAuthor() throws ServerException {
        userDao.insert(lia);
        userDao.insert(dan);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        noteDao.insert(liaNote, liaRev1);
        noteDao.update(liaNote, liaRev2);
        noteDao.insert(danNote1, danRev1);
        noteDao.insert(danNote2, danRev2);
        
        Comment com1 = new Comment(liaRev1.getCreated().plusSeconds(3), "1", admin, liaRev1);
        Comment com2 = new Comment(liaRev2.getCreated().plusSeconds(3), "2", admin, liaRev2);
        Comment com3 = new Comment(danNote1.getCreated().plusSeconds(3), "3", lia, danRev1);
        Comment com4 = new Comment(danNote2.getCreated().plusSeconds(3), "4", dan, liaRev2);
        
        commentDao.insert(com1);
        commentDao.insert(com2);
        commentDao.insert(com3);
        commentDao.insert(com4);
        
        assertAll(
                () -> assertTrue(compareCommentLists(Arrays.asList(com1, com2), commentDao.getByAuthor(admin), true)),
                () -> assertTrue(compareCommentLists(List.of(com3), commentDao.getByAuthor(lia), false)),
                () -> assertTrue(compareCommentLists(List.of(com4), commentDao.getByAuthor(dan), false))
        );
    }
    
    
    @Test
    void testGetByNoteRevision() throws ServerException {
        userDao.insert(lia);
        userDao.insert(dan);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        noteDao.insert(liaNote, liaRev1);
        noteDao.update(liaNote, liaRev2);
        noteDao.insert(danNote1, danRev1);
        noteDao.insert(danNote2, danRev2);
        
        Comment com1 = new Comment(liaRev1.getCreated().plusSeconds(3), "1", admin, liaRev1);
        Comment com2 = new Comment(liaRev1.getCreated().plusSeconds(3), "2", admin, liaRev1);
        Comment com3 = new Comment(liaRev2.getCreated().plusSeconds(3), "3", admin, liaRev2);
        Comment com4 = new Comment(danNote1.getCreated().plusSeconds(3), "4", lia, danRev1);
        Comment com5 = new Comment(danNote2.getCreated().plusSeconds(3), "5", dan, liaRev2);
        
        commentDao.insert(com1);
        commentDao.insert(com2);
        commentDao.insert(com3);
        commentDao.insert(com4);
        commentDao.insert(com5);
        
        assertAll(
                () -> assertTrue(compareCommentLists(Arrays.asList(com1, com2), commentDao.getByNoteRevision(liaRev1), true)),
                () -> assertTrue(compareCommentLists(Arrays.asList(com3, com5), commentDao.getByNoteRevision(liaRev2), true)),
                () -> assertTrue(compareCommentLists(List.of(com4), commentDao.getByNoteRevision(danRev1), false)),
                () -> assertTrue(commentDao.getByNoteRevision(danRev2).isEmpty())
        );
    }
    
    
    @Test
    void testGetByNote() throws ServerException {
        userDao.insert(lia);
        userDao.insert(dan);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        noteDao.insert(liaNote, liaRev1);
        noteDao.update(liaNote, liaRev2);
        noteDao.insert(danNote1, danRev1);
        noteDao.insert(danNote2, danRev2);
        
        Comment com1 = new Comment(liaRev1.getCreated().plusSeconds(3), "1", admin, liaRev1);
        Comment com2 = new Comment(liaRev1.getCreated().plusSeconds(3), "2", admin, liaRev1);
        Comment com3 = new Comment(liaRev2.getCreated().plusSeconds(3), "3", lia, liaRev2);
        Comment com4 = new Comment(danNote1.getCreated().plusSeconds(3), "4", lia, danRev1);
        
        commentDao.insert(com1);
        commentDao.insert(com2);
        commentDao.insert(com3);
        commentDao.insert(com4);
        
        assertAll(
                () -> assertTrue(compareCommentLists(Arrays.asList(com1, com2, com3), commentDao.getByNote(liaNote), true)),
                () -> assertTrue(compareCommentLists(List.of(com4), commentDao.getByNote(danNote1), false)),
                () -> assertTrue(commentDao.getByNote(danNote2).isEmpty())
        );
    }
    
    
    @Test
    void testDelete() throws ServerException {
        userDao.insert(lia);
        sectionDao.insert(section1);
        noteDao.insert(liaNote, liaRev1);
        Comment comm1 = new Comment(liaNote.getCreated().plusSeconds(5), "smth", lia, liaRev1);
        Comment comm2 = new Comment(liaNote.getCreated().plusSeconds(9), "smth else", lia, liaRev1);
        commentDao.insert(comm1);
        commentDao.insert(comm2);
        
        commentDao.delete(comm1);
        
        assertAll(
                () -> assertNull(commentDao.get(comm1.getId())),
                () -> assertTrue(compareComments(comm2, commentDao.get(comm2.getId())))
        );
    }
    
    
    @Test
    void testDeleteByMostRecentNoteRevision() throws ServerException {
        userDao.insert(lia);
        userDao.insert(dan);
        sectionDao.insert(section1);
        noteDao.insert(liaNote, liaRev1);
        noteDao.update(liaNote, liaRev2);
        noteDao.insert(danNote1, danRev1);
        Comment com1 = new Comment(liaRev1.getCreated().plusSeconds(3), "1", admin, liaRev1);
        Comment com2 = new Comment(liaRev1.getCreated().plusSeconds(3), "2", lia, liaRev1);
        Comment com3 = new Comment(liaRev2.getCreated().plusSeconds(3), "3", admin, liaRev2);
        Comment com4 = new Comment(danRev1.getCreated().plusSeconds(3), "4", lia, danRev1);
        commentDao.insert(com1);
        commentDao.insert(com2);
        commentDao.insert(com3);
        commentDao.insert(com4);
        
        commentDao.deleteByMostRecentNoteRevision(liaNote);
        
        assertAll(
                () -> assertNull(commentDao.get(com3.getId())),
                () -> assertTrue(compareCommentLists(Arrays.asList(com1, com2), commentDao.getByNote(liaNote), true)),
                () -> assertTrue(commentDao.getByNoteRevision(liaRev2).isEmpty()),
                () -> assertTrue(compareCommentLists(List.of(com4), commentDao.getByNote(danNote1), false))
        );
    }
}
