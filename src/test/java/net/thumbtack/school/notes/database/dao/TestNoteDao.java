package net.thumbtack.school.notes.database.dao;


import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.view.CommentView;
import net.thumbtack.school.notes.view.NoteRevisionView;
import net.thumbtack.school.notes.view.NoteView;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class TestNoteDao extends TestDaoBase {
    private final Logger LOGGER = LoggerFactory.getLogger(TestNoteDao.class);
    private static final User alex = new User("alex", "Pa55word", "Alex", null, "Astle", UserType.USER);
    private static final User irene = new User("1rene", "Pa55word", "Irene", null, "Lehtinen", UserType.USER);
    private static final Section important = new Section("Important", admin);
    private static final Section useless = new Section("Useless", alex);
    private static final Note note1 = new Note(alex, LocalDateTime.of(2020, 12, 1, 0, 2, 3), important);
    private static final Note note2 = new Note(alex, LocalDateTime.of(2021, 1, 1, 1, 3, 8), useless);
    private static final Note note3 = new Note(irene, LocalDateTime.of(2021, 1, 2, 1, 0, 9), important);
    private static final NoteRevision revision11 = new NoteRevision("note1", "revision1",
            LocalDateTime.of(2020, 12, 1, 0, 2, 3), note1);
    private static final NoteRevision revision12 = new NoteRevision("note1", "revision2",
            LocalDateTime.of(2020, 12, 1, 0, 3, 0), note1);
    private static final NoteRevision revision21 = new NoteRevision("note2", "revision1",
            LocalDateTime.of(2021, 1, 1, 1, 3, 8), note2);
    private static final NoteRevision revision31 = new NoteRevision("note3", "revision1 by Irene",
            LocalDateTime.of(2021, 1, 2, 1, 0, 9), note3);
    
    
    private boolean compareNotes(Note note1, Note note2) {
        LOGGER.debug("\n{}\n{}", note1, note2);
        LOGGER.debug("\n{}\n{}", note1.getAuthor(), note2.getAuthor());
        LOGGER.debug("\n{}\n{}", note1.getSection(), note2.getSection());
        LOGGER.debug("\n{}\n{}", note1.getNoteRevisions(), note2.getNoteRevisions());
        LOGGER.debug("\n{}\n{}", note1.getRatings(), note2.getRatings());
        
        return note1.getId() == note2.getId() &&
                note1.getAuthor().equals(note2.getAuthor()) &&
                note1.getCreated().equals(note2.getCreated()) &&
                note1.getSection().equals(note2.getSection()) &&
                note1.getNoteRevisions().equals(note2.getNoteRevisions()) &&
                note1.getRatings().equals(note2.getRatings());
    }
    
    
    private boolean compareViews(NoteView view1, NoteView view2) {
        LOGGER.debug("\n{}\n{}", view1, view2);
        
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
        
        revisions1.sort(Comparator.comparing(NoteRevisionView::getId));
        revisions2.sort(Comparator.comparing(NoteRevisionView::getId));
        comments1.sort(Comparator.comparing(CommentView::getId));
        comments2.sort(Comparator.comparing(CommentView::getId));
        
        for (int i = 0; i < revisions1.size(); i++)
            if (!compareRevisionViews(revisions1.get(i), revisions2.get(i)))
                return false;
        
        for (int i = 0; i < comments1.size(); i++)
            if (!compareCommentViews(comments1.get(i), comments2.get(i)))
                return false;
        
        return true;
    }
    
    
    private boolean compareRevisionViews(NoteRevisionView view1, NoteRevisionView view2) {
        LOGGER.debug("\n{}\n{}", view1, view2);
        
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
        
        comments1.sort(Comparator.comparing(CommentView::getId));
        comments2.sort(Comparator.comparing(CommentView::getId));
        
        for (int i = 0; i < comments1.size(); i++)
            if (!compareCommentViews(comments1.get(i), comments2.get(i)))
                return false;
        
        return true;
    }
    
    
    private boolean compareCommentViews(CommentView view1, CommentView view2) {
        LOGGER.debug("\n{}\n{}", view1, view2);
        
        return view1.getId() == view2.getId() &&
                view1.getBody().equals(view2.getBody()) &&
                view1.getAuthorId() == view2.getAuthorId() &&
                Objects.equals(view1.getNoteRevisionId(), view2.getNoteRevisionId()) &&
                view1.getCreated().equals(view2.getCreated());
    }
    
    
    private boolean compareViewLists(List<NoteView> list1, List<NoteView> list2, boolean sort) {
        LOGGER.debug("\n{}\n{}", list1, list2);
        
        if (list1.size() != list2.size())
            return false;
        
        if (sort) {
            list1.sort(Comparator.comparing(NoteView::getId));
            list2.sort(Comparator.comparing(NoteView::getId));
        }
        
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
        
        note1.setNoteRevisions(Collections.singletonList(revision11));
        note1.setRatings(Collections.emptyList());
        note3.setNoteRevisions(Collections.singletonList(revision31));
        note3.setRatings(Collections.emptyList());
        
        assertAll(
                () -> assertTrue(compareNotes(note1, noteDao.get(note1.getId()))),
                () -> assertTrue(compareNotes(note3, noteDao.get(note3.getId()))),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note1.getId(),
                                revision11.getSubject(),
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
                                revision31.getSubject(),
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
        
        note1.setNoteRevisions(Arrays.asList(revision11, revision12));
        note1.setRatings(Collections.emptyList());
        
        assertAll(
                () -> assertTrue(compareNotes(note1, noteDao.get(note1.getId()))),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note1.getId(),
                                revision12.getSubject(),
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
    void testGetAllByParams() throws ServerException {
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
                revision12.getSubject(),
                revision12.getBody(),
                important.getId(),
                alex.getId(),
                note1.getCreated(),
                0, Collections.emptyList(), Collections.emptyList()
        );
        NoteView view2 = new NoteView(
                note2.getId(),
                revision21.getSubject(),
                revision21.getBody(),
                useless.getId(),
                alex.getId(),
                note2.getCreated(),
                0, Collections.emptyList(), Collections.emptyList()
        );
        NoteView view3 = new NoteView(
                note3.getId(),
                revision31.getSubject(),
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
                                false, false, false, null, null), true),
                        "No params"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view3),
                        noteDao.getAllByParams(
                                important.getId(), null, null,
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null), true),
                        "By section"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view3, view2),
                        noteDao.getAllByParams(
                                null, "asc", null,
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null), true),
                        "Sorted by rating"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view2),
                        noteDao.getAllByParams(
                                null, null, "note2 revision2",
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null), true),
                        "By tags"),
                () -> assertTrue(compareViewLists(Collections.singletonList(view3),
                        noteDao.getAllByParams(
                                null, null, "+irene +revision1",
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null), false),
                        "By all tags 1"),
                () -> assertTrue(compareViewLists(Collections.singletonList(view3),
                        noteDao.getAllByParams(
                                null, null, "+note3 +revision1",
                                null, null,
                                null, alex.getId(), null,
                                false, false, false, null, null), false),
                        "By all tags 2"),
                () -> assertTrue(compareViewLists(Arrays.asList(view2, view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                note2.getCreated(), null,
                                null, alex.getId(), null,
                                false, false, false, null, null), true),
                        "By time from"),
                () -> assertTrue(compareViewLists(Arrays.asList(view1, view2),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, note2.getCreated(),
                                null, alex.getId(), null,
                                false, false, false, null, null), true),
                        "By time to"),
                () -> assertTrue(compareViewLists(Collections.singletonList(view2),
                        noteDao.getAllByParams(
                                null, null, null,
                                note2.getCreated().minusSeconds(1), note3.getCreated().minusSeconds(1),
                                null, alex.getId(), null,
                                false, false, false, null, null), false),
                        "By time from & time to"),
                () -> assertTrue(compareViewLists(Collections.singletonList(view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, null,
                                null, irene.getId(), "notIgnore",
                                false, false, false, null, null), false),
                        "By include: notIgnore"),
                () -> assertTrue(compareViewLists(Collections.singletonList(view3),
                        noteDao.getAllByParams(
                                null, null, null,
                                null, null,
                                null, alex.getId(), "onlyFollowings",
                                false, false, false, null, null), false),
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
    
    
    private Map<Integer, CommentView> insertAll() throws ServerException {
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
        
        return Map.of(
                11, commentView11,
                12, commentView12,
                31, commentView31
        );
    }
    
    
    @Test
    void testGetAllByParamsWithComments() throws ServerException {
        Map<Integer, CommentView> commentViews = insertAll();
        NoteView view1 = new NoteView(
                note1.getId(), revision12.getSubject(), revision12.getBody(), important.getId(), alex.getId(),
                note1.getCreated(), 0, Collections.emptyList(), Arrays.asList(commentViews.get(11), commentViews.get(12)));
        NoteView view3 = new NoteView(
                note3.getId(), revision31.getSubject(), revision31.getBody(), important.getId(), irene.getId(),
                note3.getCreated(), 0, Collections.emptyList(), Collections.singletonList(commentViews.get(31)));
        
        List<NoteView> views = noteDao.getAllByParams(
                null, null, null, null, null, null, alex.getId(), null,
                true, false, false, null, null);
        views.forEach(v -> v.getRevisions().clear());
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3), views, true));
    }
    
    
    @Test
    void testGetAllByParamsAllVersions() throws ServerException {
        insertAll();
        NoteView view1 = new NoteView(
                note1.getId(), revision12.getSubject(), revision12.getBody(), important.getId(), alex.getId(),
                note1.getCreated(), 0,
                Arrays.asList(
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
                ),
                Collections.emptyList()
        );
        NoteView view3 = new NoteView(
                note3.getId(), revision31.getSubject(), revision31.getBody(), important.getId(), irene.getId(),
                note3.getCreated(), 0,
                Collections.singletonList(
                        new NoteRevisionView(
                                revision31.getId(),
                                revision31.getBody(),
                                revision31.getCreated(),
                                Collections.emptyList())
                ),
                Collections.emptyList()
        );
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3),
                noteDao.getAllByParams(null, null, null, null, null, null, alex.getId(), null,
                        false, true, false, null, null), true));
    }
    
    
    @Test
    void testGetAllByParamsAllVersionsWithComments() throws ServerException {
        Map<Integer, CommentView> commentViews = insertAll();
        NoteView view1 = new NoteView(
                note1.getId(), revision12.getSubject(), revision12.getBody(), important.getId(), alex.getId(),
                note1.getCreated(), 0,
                Arrays.asList(
                        new NoteRevisionView(
                                revision11.getId(),
                                revision11.getBody(),
                                revision11.getCreated(),
                                Collections.singletonList(commentViews.get(11))),
                        new NoteRevisionView(
                                revision12.getId(),
                                revision12.getBody(),
                                revision12.getCreated(),
                                Collections.singletonList(commentViews.get(12)))
                ),
                Arrays.asList(commentViews.get(11), commentViews.get(12))
        );
        NoteView view3 = new NoteView(
                note3.getId(), revision31.getSubject(), revision31.getBody(), important.getId(), irene.getId(),
                note3.getCreated(), 0,
                Collections.singletonList(
                        new NoteRevisionView(
                                revision31.getId(),
                                revision31.getBody(),
                                revision31.getCreated(),
                                Collections.singletonList(commentViews.get(31)))
                ),
                Collections.singletonList(commentViews.get(31))
        );
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3),
                noteDao.getAllByParams(null, null, null, null, null, null, alex.getId(), null,
                        true, true, false, null, null), true));
    }
    
    
    @Test
    void testGetAllByParamsAllVersionsWithCommentsAndRevIdsInComments() throws ServerException {
        Map<Integer, CommentView> commentViews = insertAll();
        commentViews.get(11).setNoteRevisionId(revision11.getId());
        commentViews.get(12).setNoteRevisionId(revision12.getId());
        commentViews.get(31).setNoteRevisionId(revision31.getId());
        NoteView view1 = new NoteView(
                note1.getId(), revision12.getSubject(), revision12.getBody(), important.getId(), alex.getId(),
                note1.getCreated(), 0,
                Arrays.asList(
                        new NoteRevisionView(
                                revision11.getId(),
                                revision11.getBody(),
                                revision11.getCreated(),
                                Collections.singletonList(commentViews.get(11))),
                        new NoteRevisionView(
                                revision12.getId(),
                                revision12.getBody(),
                                revision12.getCreated(),
                                Collections.singletonList(commentViews.get(12)))
                ),
                Arrays.asList(commentViews.get(11), commentViews.get(12))
        );
        NoteView view3 = new NoteView(
                note3.getId(), revision31.getSubject(), revision31.getBody(), important.getId(), irene.getId(),
                note3.getCreated(), 0,
                Collections.singletonList(
                        new NoteRevisionView(
                                revision31.getId(),
                                revision31.getBody(),
                                revision31.getCreated(),
                                Collections.singletonList(commentViews.get(31)))
                ),
                Collections.singletonList(commentViews.get(31))
        );
        
        assertTrue(compareViewLists(Arrays.asList(view1, view3),
                noteDao.getAllByParams(null, null, null, null, null, null, alex.getId(), null,
                        true, true, true, null, null), true));
    }
    
    
    @Test
    void testDelete() throws ServerException {
        userDao.insert(alex);
        userDao.insert(irene);
        sectionDao.insert(important);
        
        noteDao.insert(note1, revision11);
        noteDao.insert(note3, revision31);
        noteDao.delete(note3);
        
        note1.setNoteRevisions(Collections.singletonList(revision11));
        note1.setRatings(Collections.emptyList());
        
        assertAll(
                () -> assertTrue(compareNotes(note1, noteDao.get(note1.getId()))),
                () -> assertNull(noteDao.get(note3.getId())),
                () -> assertTrue(compareViews(
                        new NoteView(
                                note1.getId(),
                                revision11.getSubject(),
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
