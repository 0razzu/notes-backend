package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.view.CommentView;
import net.thumbtack.school.notes.view.NoteRevisionView;
import net.thumbtack.school.notes.view.NoteView;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class TestNoteDao extends TestDaoBase {
    private static final User alex = new User("alex", "Pa55word", "Alex", null, "Astle", UserType.USER);
    private static final User irene = new User("1rene", "Pa55word", "Irene", null, "Lehtinen", UserType.USER);
    private static final Section important = new Section("Important", admin);
    private static final Section useless = new Section("Useless", alex);
    private static final Note note1 = new Note("subj1", alex, LocalDateTime.of(2020, 12, 1, 0, 2, 3), important);
    private static final Note note2 = new Note("subj2", alex, LocalDateTime.of(2021, 1, 1, 1, 3, 8), useless);
    private static final Note note3 = new Note("subj3", irene, LocalDateTime.of(2021, 1, 2, 1, 0, 9), important);
    private static final NoteRevision revision11 = new NoteRevision("note1 revision1",
            LocalDateTime.of(2020, 12, 1, 0, 2, 3), note1);
    private static final NoteRevision revision12 = new NoteRevision("note1 revision2",
            LocalDateTime.of(2020, 12, 1, 0, 3, 0), note1);
    private static final NoteRevision revision21 = new NoteRevision("note2 revision1",
            LocalDateTime.of(2021, 1, 1, 1, 3, 8), note2);
    private static final NoteRevision revision31 = new NoteRevision("note3 revision1",
            LocalDateTime.of(2021, 1, 2, 1, 0, 9), note3);
    
    
    private boolean compareNotes(Note note1, Note note2) {
        return note1.getId() == note2.getId() &&
                note1.getSubject().equals(note2.getSubject()) &&
                note1.getAuthor().equals(note2.getAuthor()) &&
                note1.getCreated().equals(note2.getCreated()) &&
                note1.getSection().equals(note2.getSection()) &&
                note1.getNoteRevisions().equals(note2.getNoteRevisions()) &&
                note1.getRatings().equals(note2.getRatings());
    }
    
    
    private boolean compareViews(NoteView view1, NoteView view2) {
        if (view1.getId() != view2.getId() ||
                !view1.getSubject().equals(view2.getSubject()) ||
                !view1.getBody().equals(view2.getBody()) ||
                view1.getSectionId() != view2.getSectionId() ||
                view1.getAuthorId() != view2.getAuthorId() ||
                !view1.getCreated().equals(view2.getCreated()) ||
                view1.getRevisionId() != view2.getRevisionId())
            return false;
        
        List<NoteRevisionView> revisions1 = view1.getRevisions();
        List<NoteRevisionView> revisions2 = view2.getRevisions();
        List<CommentView> comments1 = view1.getComments();
        List<CommentView> comments2 = view2.getComments();
        
        if (revisions1 == null)
            return revisions2 == null;
        
        if (revisions1.size() != revisions2.size())
            return false;
        
        if (comments1 == null)
            return comments2 == null;
        
        if (comments1.size() != comments2.size())
            return false;
        
        for (int i = 0; i < revisions1.size(); i++)
            if (!compareRevisionViews(revisions1.get(i), revisions2.get(i)))
                return false;
        
        for (int i = 0; i < comments1.size(); i++)
            if (!compareCommentViews(comments1.get(i), comments2.get(i)))
                return false;
        
        return true;
    }
    
    
    private boolean compareRevisionViews(NoteRevisionView view1, NoteRevisionView view2) {
        if (view1.getId() != view2.getId() ||
                !view1.getBody().equals(view2.getBody()) ||
                !view1.getCreated().equals(view2.getCreated()))
            return false;
        
        List<CommentView> comments1 = view1.getComments();
        List<CommentView> comments2 = view2.getComments();
        
        if (comments1 == null)
            return comments2 == null;
        
        if (comments1.size() != comments2.size())
            return false;
        
        for (int i = 0; i < comments1.size(); i++)
            if (!compareCommentViews(comments1.get(i), comments2.get(i)))
                return false;
        
        return true;
    }
    
    
    private boolean compareCommentViews(CommentView view1, CommentView view2) {
        return view1.getId() == view2.getId() &&
                view1.getBody().equals(view2.getBody()) &&
                view1.getAuthorId() == view2.getAuthorId() &&
                Objects.equals(view1.getNoteRevisionId(), view2.getNoteRevisionId()) &&
                view1.getCreated().equals(view2.getCreated());
    }
    
    
    private boolean compareViewLists(List<NoteView> list1, List<NoteView> list2) {
        if (list1.size() != list2.size())
            return false;
        
        for (int i = 0; i < list1.size(); i++)
            if (!compareViews(list1.get(i), list2.get(i)))
                return false;
        
        return true;
    }
    
    
    @Test
    void testInsert() throws ServerException {
        userDao.insert(alex);
        userDao.insert(irene);
        sectionDao.insert(important);
        
        noteDao.insert(note1, revision11);
        noteDao.insert(note3, revision31);
        
        note1.setNoteRevisions(List.of(revision11));
        note1.setRatings(Collections.emptyList());
        note3.setNoteRevisions(List.of(revision31));
        note3.setRatings(Collections.emptyList());
        
        assertAll(
                () -> assertTrue(compareNotes(note1, noteDao.get(note1.getId()))),
                () -> assertTrue(compareNotes(note3, noteDao.get(note3.getId()))),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note1.getId(),
                                note1.getSubject(),
                                revision11.getBody(),
                                note1.getSection().getId(),
                                note1.getAuthor().getId(),
                                note1.getCreated(),
                                revision11.getId(),
                                null, null
                        ), noteDao.getView(note1.getId()))),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note3.getId(),
                                note3.getSubject(),
                                revision31.getBody(),
                                note3.getSection().getId(),
                                note3.getAuthor().getId(),
                                note3.getCreated(),
                                revision31.getId(),
                                null, null
                        ), noteDao.getView(note3.getId())))
        );
    }
    
    
    @Test
    void testUpdate() throws ServerException {
        userDao.insert(alex);
        sectionDao.insert(important);
        
        noteDao.insert(note1, revision11);
        noteDao.update(note1, revision12);
        
        note1.setNoteRevisions(List.of(revision11, revision12));
        note1.setRatings(Collections.emptyList());
        
        assertAll(
                () -> assertTrue(compareNotes(note1, noteDao.get(note1.getId()))),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note1.getId(),
                                note1.getSubject(),
                                revision12.getBody(),
                                note1.getSection().getId(),
                                note1.getAuthor().getId(),
                                note1.getCreated(),
                                revision12.getId(),
                                null, null
                        ), noteDao.getView(note1.getId())))
        );
    }
    
    
    @Test
    void testGetAllByParams1() throws ServerException {
        userDao.insert(alex);
        userDao.insert(irene);
        userDao.follow(alex, irene);
        userDao.ignore(irene, alex);
        sectionDao.insert(important);
        sectionDao.insert(useless);
        noteDao.insert(note1, revision11);
        noteDao.update(note1, revision12);
        noteDao.insert(note2, revision21);
        noteDao.insert(note3, revision31);
        ratingDao.insert(new Rating(5, admin), note2);
        ratingDao.insert(new Rating(3, admin), note3);
        
        NoteView view1 = new NoteView(
                note1.getId(),
                note1.getSubject(),
                revision12.getBody(),
                important.getId(),
                alex.getId(),
                note1.getCreated(),
                0, Collections.emptyList(), Collections.emptyList()
        );
        
        NoteView view2 = new NoteView(
                note2.getId(),
                note2.getSubject(),
                revision21.getBody(),
                useless.getId(),
                alex.getId(),
                note2.getCreated(),
                0, Collections.emptyList(), Collections.emptyList()
        );
        
        NoteView view3 = new NoteView(
                note3.getId(),
                note3.getSubject(),
                revision31.getBody(),
                important.getId(),
                irene.getId(),
                note3.getCreated(),
                0, Collections.emptyList(), Collections.emptyList()
        );
        
        assertAll(
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view2, view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "No params"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view3),
                        noteDao.getAllByParams(
                                important.getId(), null, null,
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "By section"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view3, view2),
                        noteDao.getAllByParams(
                                null, "asc", null,
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "Sorted by rating"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view2),
                        noteDao.getAllByParams(
                                null, null, "note2 revision2",
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "By tags"),
                () -> assertTrue(compareViewLists(List.of(view3),
                        noteDao.getAllByParams(
                                null, null, "+note3 +revision1",
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "By all tags"),
                () -> assertTrue(compareViewLists(Arrays.asList(view2, view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                note2.getCreated(), null,
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "By time from"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view2),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, note2.getCreated(),
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "By time to"),
                () -> assertTrue(compareViewLists(List.of(view2),
                        noteDao.getAllByParams(
                                null, null, null,
                                note2.getCreated().minusSeconds(1), note3.getCreated().minusSeconds(1),
                                null, alex.getId(), null,
                                false, false, false, null, null)),
                        "By time from & time to"),
                () -> assertTrue(compareViewLists(List.of(view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, null,
                                null, irene.getId(), "notIgnore",
                                false, false, false, null, null)),
                        "By include: notIgnore"),
                () -> assertTrue(compareViewLists(List.of(view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, null,
                                null, alex.getId(), "onlyFollowings",
                                false, false, false, null, null)),
                        "By include: onlyFollowings"),
                () -> assertEquals(2, noteDao.getAllByParams(
                        null, null, null,
                        null, null,
                        null, alex.getId(), null,
                        false, false, false, 1, null).size(),
                        "By from"),
                () -> assertEquals(2, noteDao.getAllByParams(
                        null, null, null,
                        null, null,
                        null, alex.getId(), null,
                        false, false, false, null, 2).size(),
                        "By count"),
                () -> assertEquals(1, noteDao.getAllByParams(
                        null, null, null,
                        null, null,
                        null, alex.getId(), null,
                        false, false, false, 2, 2).size(),
                        "By from & count")
        );
    }
    
    
    @Test
    void testGetAllByParams2() throws ServerException {
        Comment comment11 = new Comment(LocalDateTime.of(2020, 12, 1, 0, 2, 10),
                "comm11", admin, revision11);
        Comment comment12 = new Comment(LocalDateTime.of(2021, 1, 5, 0, 0, 0),
                "comm12", irene, revision12);
        Comment comment31 = new Comment(LocalDateTime.of(2021, 1, 5, 1, 0, 0),
                "comm31", alex, revision31);
        
        userDao.insert(alex);
        userDao.insert(irene);
        sectionDao.insert(important);
        noteDao.insert(note1, revision11);
        commentDao.insert(comment11);
        noteDao.update(note1, revision12);
        noteDao.insert(note3, revision31);
        commentDao.insert(comment12);
        commentDao.insert(comment31);
        
        CommentView commentView11 = new CommentView(
                comment11.getId(),
                comment11.getBody(),
                comment11.getAuthor().getId(),
                null,
                comment11.getCreated()
        );
        CommentView commentView12 = new CommentView(
                comment12.getId(),
                comment12.getBody(),
                comment12.getAuthor().getId(),
                null,
                comment12.getCreated()
        );
        CommentView commentView31 = new CommentView(
                comment31.getId(),
                comment31.getBody(),
                comment31.getAuthor().getId(),
                null,
                comment31.getCreated()
        );
        
        NoteView view1 = new NoteView(
                note1.getId(),
                note1.getSubject(),
                revision12.getBody(),
                important.getId(),
                alex.getId(),
                note1.getCreated(),
                0, Collections.emptyList(), Arrays.asList(commentView11, commentView12)
        );
        NoteView view3 = new NoteView(
                note3.getId(),
                note3.getSubject(),
                revision31.getBody(),
                important.getId(),
                irene.getId(),
                note3.getCreated(),
                0, Collections.emptyList(), List.of(commentView31)
        );
        
        List<NoteView> views = noteDao.getAllByParams(
                null, null, null, null, null, null, alex.getId(), null,
                true, false, false, null, null);
        views.forEach(v -> v.getRevisions().clear());
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3), views), "With comments");
        
        views = noteDao.getAllByParams(
                null, null, null, null, null, null, alex.getId(), null,
                false, true, false, null, null);
        view1.setRevisions(List.of(
                new NoteRevisionView(
                        revision11.getId(),
                        revision11.getBody(),
                        revision11.getCreated(),
                        Collections.emptyList()),
                new NoteRevisionView(
                        revision12.getId(),
                        revision12.getBody(),
                        revision12.getCreated(),
                        Collections.emptyList())
        ));
        view1.setComments(Collections.emptyList());
        view3.setRevisions(List.of(
                new NoteRevisionView(
                        revision31.getId(),
                        revision31.getBody(),
                        revision31.getCreated(),
                        Collections.emptyList())
        ));
        view3.setComments(Collections.emptyList());
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3), views), "All versions");
        
        views = noteDao.getAllByParams(
                null, null, null, null, null, null, alex.getId(), null,
                true, true, false, null, null);
        view1.setRevisions(List.of(
                new NoteRevisionView(
                        revision11.getId(),
                        revision11.getBody(),
                        revision11.getCreated(),
                        List.of(commentView11)),
                new NoteRevisionView(
                        revision12.getId(),
                        revision12.getBody(),
                        revision12.getCreated(),
                        List.of(commentView12))
        ));
        view1.setComments(List.of(commentView11, commentView12));
        view3.setRevisions(List.of(
                new NoteRevisionView(
                        revision31.getId(),
                        revision31.getBody(),
                        revision31.getCreated(),
                        List.of(commentView31))
        ));
        view3.setComments(List.of(commentView31));
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3), views), "All versions with comments");
        
        views = noteDao.getAllByParams(
                null, null, null, null, null, null, alex.getId(), null,
                true, true, true, null, null);
        commentView11.setNoteRevisionId(revision11.getId());
        commentView12.setNoteRevisionId(revision12.getId());
        commentView31.setNoteRevisionId(revision31.getId());
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3), views),
                "All versions with comments & revision ids in comments");
    }
    
    
    @Test
    void testDelete() throws ServerException {
        userDao.insert(alex);
        userDao.insert(irene);
        sectionDao.insert(important);
        
        noteDao.insert(note1, revision11);
        noteDao.insert(note3, revision31);
        noteDao.delete(note3);
        
        note1.setNoteRevisions(List.of(revision11));
        note1.setRatings(Collections.emptyList());
        
        assertAll(
                () -> assertTrue(compareNotes(note1, noteDao.get(note1.getId()))),
                () -> assertNull(noteDao.get(note3.getId())),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note1.getId(),
                                note1.getSubject(),
                                revision11.getBody(),
                                note1.getSection().getId(),
                                note1.getAuthor().getId(),
                                note1.getCreated(),
                                revision11.getId(),
                                null, null
                        ), noteDao.getView(note1.getId()))),
                () -> assertNull(noteDao.getView(note3.getId()))
        );
    }
}
