package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.dto.request.CreateCommentRequest;
import net.thumbtack.school.notes.dto.request.EditCommentRequest;
import net.thumbtack.school.notes.dto.response.CreateCommentResponse;
import net.thumbtack.school.notes.dto.response.EditCommentResponse;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import net.thumbtack.school.notes.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCode.NOT_BLANK;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.COMMENT_NOT_FOUND;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.NOTE_NOT_FOUND_NOTE_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestCommentsController extends TestControllerBase {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CommentDao commentDao;
    @MockBean
    private NoteDao noteDao;
    @MockBean
    private NoteRevisionDao noteRevisionDao;
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    
    private static final User helen = new User(2, "helen", "J3K9n1ye", "Helen", null, "Sykes", UserType.USER);
    private static final User dorte = new User(3, "d0rt3", "H80-1h21", "Dorte", null, "Lund", UserType.USER);
    private static final Section section = new Section("Admin section", admin);
    private static final Note note = new Note(5, dorte, LocalDateTime.of(2020, 8, 15, 23, 24, 25), section, null, null);
    private static final NoteRevision rev = new NoteRevision(1, "To do", "nothing", note.getCreated(), note, null);
    private static final Comment adminCom = new Comment(3, note.getCreated(), "smth", admin, rev);
    private static final Comment helenCom = new Comment(4, note.getCreated(), "smth 2", helen, rev);
    
    
    @Test
    void testCreate() throws Exception {
        clearInvocations(commentDao);
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(noteDao.get(5)).thenReturn(note);
        when(noteRevisionDao.getMostRecent(note)).thenReturn(rev);
        when(sessionDao.getUser(anyString())).thenReturn(helen);
        
        MockHttpServletResponse response = mvc.perform(post("/api/comments")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateCommentRequest("smth", 5)))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        CreateCommentResponse comResponse = mapper.readValue(response.getContentAsString(), CreateCommentResponse.class);
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals("smth", comResponse.getBody()),
                () -> assertEquals(5, comResponse.getNoteId()),
                () -> assertEquals(2, comResponse.getAuthorId()),
                () -> assertEquals(1, comResponse.getRevisionId())
        );
        
        verify(commentDao).insert(any());
        verify(noteDao).get(5);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testCreateConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/comments")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateCommentRequest("", 1)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_BLANK", "body", NOT_BLANK.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testCreateNoNote() throws Exception {
        when(noteDao.get(1)).thenReturn(null);
        when(sessionDao.getUser(anyString())).thenReturn(helen);
        
        MockHttpServletResponse response = mvc.perform(post("/api/comments")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateCommentRequest("smth", 1)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOTE_NOT_FOUND_NOTE_ID", "noteId", NOTE_NOT_FOUND_NOTE_ID.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testCreateNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/comments")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new CreateCommentRequest("smth", 1)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testEdit() throws Exception {
        clearInvocations(commentDao);
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(commentDao.get(3)).thenReturn(adminCom);
        when(noteRevisionDao.getMostRecent(note)).thenReturn(rev);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(put("/api/comments/3")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditCommentRequest("smth 1")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        EditCommentResponse comResponse = mapper.readValue(response.getContentAsString(), EditCommentResponse.class);
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals("smth 1", comResponse.getBody()),
                () -> assertEquals(5, comResponse.getNoteId()),
                () -> assertEquals(0, comResponse.getAuthorId()),
                () -> assertEquals(1, comResponse.getRevisionId())
        );
        
        verify(commentDao).update(any());
        verify(commentDao).get(3);
        verify(noteRevisionDao).getMostRecent(note);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testEditConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/comments/3")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditCommentRequest("")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_BLANK", "body", NOT_BLANK.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testEditNoComment() throws Exception {
        when(commentDao.get(1)).thenReturn(null);
        when(sessionDao.getUser(anyString())).thenReturn(helen);
        
        MockHttpServletResponse response = mvc.perform(put("/api/comments/1")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditCommentRequest("smth")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("COMMENT_NOT_FOUND", "id", COMMENT_NOT_FOUND.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testEditOtherUsersComment() throws Exception {
        when(commentDao.get(4)).thenReturn(helenCom);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(put("/api/comments/4")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditCommentRequest("smth")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testEditNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/comments/1")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new EditCommentRequest("smth")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testDelete() throws Exception {
        clearInvocations(commentDao);
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(commentDao.get(4)).thenReturn(helenCom);
        when(sessionDao.getUser(anyString())).thenReturn(helen);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/comments/4").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(commentDao).delete(any());
        verify(commentDao).get(4);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testDeleteOtherUsersComment() throws Exception {
        when(commentDao.get(3)).thenReturn(adminCom);
        when(sessionDao.getUser(anyString())).thenReturn(helen);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/comments/3").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testDeleteOtherUsersCommentByAdmin() throws Exception {
        clearInvocations(commentDao);
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(commentDao.get(4)).thenReturn(helenCom);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/comments/4").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(commentDao).delete(any());
        verify(commentDao).get(4);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testDeleteOtherUsersCommentByNoteAuthor() throws Exception {
        clearInvocations(commentDao);
        clearInvocations(noteDao);
        clearInvocations(sessionDao);
        when(commentDao.get(3)).thenReturn(adminCom);
        when(sessionDao.getUser(anyString())).thenReturn(dorte);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/comments/3").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(commentDao).delete(any());
        verify(commentDao).get(3);
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testDeleteNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/comments/3")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
}
