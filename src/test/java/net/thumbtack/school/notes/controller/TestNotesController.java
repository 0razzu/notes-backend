package net.thumbtack.school.notes.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.RateNoteRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.view.CommentView;
import net.thumbtack.school.notes.view.NoteRevisionView;
import net.thumbtack.school.notes.view.NoteView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCode.*;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestNotesController extends TestControllerBase {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CommentDao commentDao;
    @MockBean
    private NoteDao noteDao;
    @MockBean
    private RatingDao ratingDao;
    @MockBean
    private SectionDao sectionDao;
    @MockBean
    private SessionDao sessionDao;
    
    private static final Set<ErrorResponse> noNoteSet =
            Set.of(new ErrorResponse("NOTE_NOT_FOUND", "id", NOTE_NOT_FOUND.getMessage()));
    private static final User bill = new User("bill", "Bill1234", "Bill", null, "Blare", UserType.USER);
    private static final Section admSection = new Section("Adm section", admin);
    private static final Section billSection = new Section("Bill section", bill);
    private static final Note admNote1 = new Note(admin, null, admSection);
    private static final Note admNote2 = new Note(admin, null, billSection);
    private static final Note billNote = new Note(bill, LocalDateTime.of(2020, 12, 1, 2, 1), billSection);
    private static final NoteRevision admRev1 = new NoteRevision("note 1", "smth", null, admNote1);
    private static final NoteRevision admRev2 = new NoteRevision("note 2", "smth 2", null, admNote2);
    private static final NoteView admView1 = new NoteView(1, admRev1.getSubject(), admRev1.getBody(), 0, 0,
            LocalDateTime.of(2020, 12, 1, 2, 1), 1,
            List.of(new NoteRevisionView(1, admRev1.getBody(), admRev1.getCreated(), Collections.emptyList())),
            Collections.emptyList());
    private static final Comment com1 = new Comment(1, LocalDateTime.of(2020, 12, 31, 2, 4), "comment 1", admin, admRev1);
    private static final Comment com2 = new Comment(2, LocalDateTime.of(2021, 1, 2, 1, 2), "comment 2", bill, admRev2);
    
    
    @Test
    void testCreate() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(sectionDao.get(0)).thenReturn(admSection);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(post("/api/notes")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateNoteRequest("note 1", "smth", 0)))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        CreateNoteResponse noteResponse = mapper.readValue(response.getContentAsString(), CreateNoteResponse.class);
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals("note 1", noteResponse.getSubject()),
                () -> assertEquals("smth", noteResponse.getBody())
        );
        
        verify(noteDao).insert(any(), any());
        verify(sectionDao).get(0);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testCreateConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/notes")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateNoteRequest("", null, 0)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_BLANK", "subject", NOT_BLANK.getMessage()),
                new ErrorResponse("NOT_BLANK", "body", NOT_BLANK.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testCreateNoSection() throws Exception {
        when(sectionDao.get(0)).thenReturn(null);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(post("/api/notes")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateNoteRequest("note 1", "smth", 0)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("SECTION_NOT_FOUND", "sectionId", SECTION_NOT_FOUND.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testCreateNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/notes")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateNoteRequest("note 1", "smth", 0)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testGet() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(noteDao.getView(1)).thenReturn(admView1);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/notes/1").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new GetNoteResponse(
                        admView1.getId(),
                        admView1.getSubject(),
                        admView1.getBody(),
                        admView1.getSectionId(),
                        admView1.getAuthorId(),
                        admView1.getCreated(),
                        admView1.getRevisionId()
                ), mapper.readValue(response.getContentAsString(), GetNoteResponse.class))
        );
        
        verify(noteDao).getView(1);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testGetNoNote() throws Exception {
        when(noteDao.getView(1)).thenReturn(null);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/notes/1").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noNoteSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/notes/0")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testEditBody() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(noteDao.get(1)).thenReturn(admNote1);
        when(noteDao.getView(1)).thenReturn(admView1);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(put("/api/notes/1")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditNoteRequest("changed", null)))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EditNoteResponse(
                        admView1.getId(),
                        admView1.getSubject(),
                        "changed",
                        admView1.getSectionId(),
                        admView1.getAuthorId(),
                        admView1.getCreated(),
                        admView1.getRevisionId()
                ), mapper.readValue(response.getContentAsString(), EditNoteResponse.class))
        );
        
        verify(noteDao).update(any(), any());
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testEditSection() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(noteDao.get(1)).thenReturn(admNote1);
        when(noteDao.getView(1)).thenReturn(admView1);
        when(sectionDao.get(2)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(put("/api/notes/1")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditNoteRequest(null, 2)))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EditNoteResponse(
                        admView1.getId(),
                        admView1.getSubject(),
                        admView1.getBody(),
                        2,
                        admView1.getAuthorId(),
                        admView1.getCreated(),
                        admView1.getRevisionId()
                ), mapper.readValue(response.getContentAsString(), EditNoteResponse.class))
        );
        
        verify(noteDao).update(any(), any());
        verify(sectionDao).get(2);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testEditBodyAndSection() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sectionDao);
        clearInvocations(sessionDao);
        when(noteDao.get(1)).thenReturn(admNote1);
        when(noteDao.getView(1)).thenReturn(admView1);
        when(sectionDao.get(2)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(put("/api/notes/1")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditNoteRequest("upd", 2)))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EditNoteResponse(
                        admView1.getId(),
                        admView1.getSubject(),
                        "upd",
                        2,
                        admView1.getAuthorId(),
                        admView1.getCreated(),
                        admView1.getRevisionId()
                ), mapper.readValue(response.getContentAsString(), EditNoteResponse.class))
        );
        
        verify(noteDao).update(any(), any());
        verify(sectionDao).get(2);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testEditConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/notes/1")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditNoteRequest("", 2)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_BLANK", "body", NOT_BLANK.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testEditOtherUsersNote() throws Exception {
        when(noteDao.get(1)).thenReturn(admNote1);
        when(noteDao.getView(1)).thenReturn(admView1);
        when(sectionDao.get(2)).thenReturn(billSection);
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(put("/api/notes/1")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditNoteRequest("upd", 2)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_PERMITTED", JAVA_SESSION_ID, NOT_PERMITTED.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testEditNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/notes/0")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditNoteRequest("upd", 2)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testDelete() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(noteDao.get(1)).thenReturn(admNote1);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/notes/1").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(noteDao).delete(admNote1);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testDeleteOtherUsersNote() throws Exception {
        when(noteDao.get(1)).thenReturn(admNote1);
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/notes/1").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testDeleteOtherUsersNoteByAdmin() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(noteDao.get(1)).thenReturn(billNote);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/notes/1").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(noteDao).delete(billNote);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testDeleteNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/notes/1")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetComments() throws Exception {
        clearInvocations(commentDao);
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(commentDao.getByNote(admNote1)).thenReturn(List.of(com1, com2));
        when(noteDao.get(1)).thenReturn(admNote1);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/notes/1/comments").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        List<GetNoteCommentsResponseItem> comments = mapper.readValue(response.getContentAsString(),
                new TypeReference<>() {});
        GetNoteCommentsResponseItem comResponse1 = comments.get(0);
        GetNoteCommentsResponseItem comResponse2 = comments.get(1);
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(com1.getId(), comResponse1.getId()),
                () -> assertEquals(com1.getBody(), comResponse1.getBody()),
                () -> assertEquals(com1.getAuthor().getId(), comResponse1.getAuthorId()),
                () -> assertEquals(com1.getNoteRevision().getId(), comResponse1.getRevisionId()),
                () -> assertEquals(com1.getCreated(), comResponse1.getCreated()),
                () -> assertEquals(com2.getId(), comResponse2.getId()),
                () -> assertEquals(com2.getBody(), comResponse2.getBody()),
                () -> assertEquals(com2.getAuthor().getId(), comResponse2.getAuthorId()),
                () -> assertEquals(com2.getNoteRevision().getId(), comResponse2.getRevisionId()),
                () -> assertEquals(com2.getCreated(), comResponse2.getCreated())
        );
        
        verify(commentDao).getByNote(admNote1);
        verify(noteDao).get(1);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testGetCommentsNoNote() throws Exception {
        when(noteDao.get(1)).thenReturn(null);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/notes/1/comments").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noNoteSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetCommentsNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/notes/1/comments")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testRate() throws Exception {
        clearInvocations(noteDao);
        clearInvocations(ratingDao);
        clearInvocations(sessionDao);
        when(noteDao.get(1)).thenReturn(admNote1);
        when(sessionDao.getUser(anyString())).thenReturn(bill);
        
        MockHttpServletResponse response = mvc.perform(post("/api/notes/1/rating")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RateNoteRequest(5)))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(noteDao).get(1);
        verify(ratingDao).insert(new Rating(5, bill), admNote1);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testRateConstraints() throws Exception {
        MockHttpServletResponse response1 = mvc.perform(post("/api/notes/1/rating")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RateNoteRequest(0)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        MockHttpServletResponse response2 = mvc.perform(post("/api/notes/1/rating")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RateNoteRequest(6)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertAll(
                () -> assertEquals(Set.of(
                        new ErrorResponse("MIN_CONSTRAINT_VIOLATION", "rating", MIN_CONSTRAINT_VIOLATION.getMessage())
                ), getErrorSet(response1)),
                () -> assertEquals(Set.of(
                        new ErrorResponse("MAX_CONSTRAINT_VIOLATION", "rating", MAX_CONSTRAINT_VIOLATION.getMessage())
                ), getErrorSet(response2))
        );
    }
    
    
    @Test
    void testRateNoNote() throws Exception {
        when(noteDao.get(1)).thenReturn(null);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(post("/api/notes/1/rating")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RateNoteRequest(5)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noNoteSet, getErrorSet(response));
    }
    
    
    @Test
    void testRateSelfRating() throws Exception {
        when(noteDao.get(1)).thenReturn(admNote1);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(post("/api/notes/1/rating")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RateNoteRequest(5)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testRateNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/notes/1/rating")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new RateNoteRequest(5)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetNotesResponse() throws Exception {
        CommentView comView1 = new CommentView(1, "com 1", 1, 1, billNote.getCreated().plusHours(1));
        CommentView comView2 = new CommentView(2, "com 2", 1, 2, billNote.getCreated().plusHours(2));
        CommentView comView3 = new CommentView(3, "com 3", 2, 2, billNote.getCreated().plusHours(3));
        
        NoteRevisionView billRev1 = new NoteRevisionView(1, "smth 3", billNote.getCreated(),
                List.of(comView1));
        NoteRevisionView billRev2 = new NoteRevisionView(2, "smth 4", billNote.getCreated().plusSeconds(2),
                List.of(comView2, comView3));
        NoteView billView = new NoteView(2, "note 3", billRev2.getBody(), 2, 2,
                billNote.getCreated(), 2, List.of(billRev1, billRev2), List.of(comView1, comView2, comView3));
        
        GetNotesResponseItemComment comResponse1 = new GetNotesResponseItemComment(1, "com 1", 1, 1,
                billNote.getCreated().plusHours(1));
        GetNotesResponseItemComment comResponse2 = new GetNotesResponseItemComment(2, "com 2", 1, 2,
                billNote.getCreated().plusHours(2));
        GetNotesResponseItemComment comResponse3 = new GetNotesResponseItemComment(3, "com 3", 2, 2,
                billNote.getCreated().plusHours(3));
        
        GetNotesResponseItem admNoteResponse = new GetNotesResponseItem(
                admView1.getId(),
                admView1.getSubject(),
                admView1.getBody(),
                admView1.getSectionId(),
                admin.getId(),
                admView1.getCreated(),
                List.of(
                        new GetNotesResponseItemRevision(
                                1,
                                admRev1.getBody(),
                                admRev1.getCreated(),
                                Collections.emptyList()
                        )
                ),
                Collections.emptyList()
        );
        GetNotesResponseItem billNoteResponse = new GetNotesResponseItem(
                billView.getId(),
                billView.getSubject(),
                billView.getBody(),
                billView.getSectionId(),
                billView.getAuthorId(),
                billView.getCreated(),
                List.of(
                        new GetNotesResponseItemRevision(
                                billRev1.getId(),
                                billRev1.getBody(),
                                billRev1.getCreated(),
                                List.of(comResponse1)
                        ),
                        new GetNotesResponseItemRevision(
                                billRev2.getId(),
                                billRev2.getBody(),
                                billRev2.getCreated(),
                                List.of(comResponse2, comResponse3)
                        )
                ),
                List.of(comResponse1, comResponse2, comResponse3)
        );
        
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(noteDao.getAllByParams(
                any(), any(), any(), any(), any(),
                any(), anyInt(), any(),
                anyBoolean(), anyBoolean(), anyBoolean(),
                any(), any())).thenReturn(List.of(admView1, billView));
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("allVersions", "true")
                .param("comments", "true")
                .param("commentVersion", "true")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(mapper.readTree(toJson(List.of(admNoteResponse, billNoteResponse))),
                        mapper.readTree(response.getContentAsString()))
        );
        
        verify(noteDao).getAllByParams(
                null, null, null,
                null, null, null, 0, null,
                true, true, true, null, null
        );
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testGetNotesParams() throws Exception {
        clearInvocations(noteDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("sectionId", "8")
                .param("sortByRating", "desc")
                .param("tags", "admin", "important")
                .param("allTags", "true")
                .param("timeFrom", "2020-01-03T15:02:19")
                .param("timeTo", "2021-02-18T00:01:42")
                .param("user", "9")
                .param("include", "onlyFollowings")
                .param("comments", "true")
                .param("allVersions", "true")
                .param("commentVersion", "true")
                .param("from", "20")
                .param("count", "5")
        ).andExpect(status().isOk());
        
        verify(noteDao).getAllByParams(
                8, "desc", "+admin +important",
                LocalDateTime.of(2020, 1, 3, 15, 2, 19),
                LocalDateTime.of(2021, 2, 18, 0, 1, 42),
                9, admin.getId(), "onlyFollowings",
                true, true, true, 20, 5
        );
    }
    
    
    @Test
    void testGetNotesParamDefaultValues() throws Exception {
        clearInvocations(noteDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        mvc.perform(get("/api/notes").cookie(cookie)
        ).andExpect(status().isOk());
        
        verify(noteDao).getAllByParams(
                null, null, null,
                null, null,
                null, admin.getId(), null,
                false, false, false, null, null
        );
    }
    
    
    @Test
    void testGetNotesConstraints() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response1 = mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("sortByRating", "www")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        MockHttpServletResponse response2 = mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("include", "all")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        MockHttpServletResponse response3 = mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("from", "-1")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        MockHttpServletResponse response4 = mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("count", "0")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        MockHttpServletResponse response5 = mvc.perform(get("/api/notes")
                .cookie(cookie)
                .param("timeFrom", "1:2:3 1.11.2020")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertAll(
                () -> assertEquals(Set.of(
                        new ErrorResponse("SORTING_CONSTRAINT_VIOLATION", "sortByRating",
                                SORTING_CONSTRAINT_VIOLATION.getMessage())
                ), getErrorSet(response1)),
                () -> assertEquals(Set.of(
                        new ErrorResponse("INCLUDE_CONSTRAINT_VIOLATION", "include",
                                INCLUDE_CONSTRAINT_VIOLATION.getMessage())
                ), getErrorSet(response2)),
                () -> assertEquals(Set.of(
                        new ErrorResponse("MIN_CONSTRAINT_VIOLATION", "from",
                                MIN_CONSTRAINT_VIOLATION.getMessage())
                ), getErrorSet(response3)),
                () -> assertEquals(Set.of(
                        new ErrorResponse("MIN_CONSTRAINT_VIOLATION", "count",
                                MIN_CONSTRAINT_VIOLATION.getMessage())
                ), getErrorSet(response4)),
                () -> assertEquals(Set.of(
                        new ErrorResponse("TYPE_MISMATCH", "timeFrom",
                                TYPE_MISMATCH.getMessage())
                ), getErrorSet(response5))
        );
    }
    
    
    @Test
    void testGetNotesNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/notes")
                .param("from", "1")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
}
