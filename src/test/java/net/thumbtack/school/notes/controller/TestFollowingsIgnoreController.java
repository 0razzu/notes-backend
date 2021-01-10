package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.dto.request.FollowUserRequest;
import net.thumbtack.school.notes.dto.request.IgnoreUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.USER_NOT_FOUND_BY_LOGIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestFollowingsIgnoreController extends TestControllerBase {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    
    private static final Set<ErrorResponse> noUserSet = Set.of(
            new ErrorResponse("USER_NOT_FOUND_BY_LOGIN", "login", USER_NOT_FOUND_BY_LOGIN.getMessage()));
    private static final User alice = new User("alice", "Ju3410K8f", "Alice", null, "Brown", UserType.USER);
    private static final User bob = new User("bob", "3refd219", "Bob", null, "Nitt", UserType.USER);
    
    
    @Test
    void testFollow() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(bob);
        
        MockHttpServletResponse response = mvc.perform(post("/api/followings")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new FollowUserRequest("bob")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).follow(alice, bob);
    }
    
    
    @Test
    void testFollowNoUser() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(null);
        
        MockHttpServletResponse response = mvc.perform(post("/api/followings")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new FollowUserRequest("bob")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noUserSet, getErrorSet(response));
    }
    
    
    @Test
    void testFollowNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/followings")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new FollowUserRequest("bob")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testIgnore() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(bob);
        
        MockHttpServletResponse response = mvc.perform(post("/api/ignore")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new IgnoreUserRequest("bob")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).ignore(alice, bob);
    }
    
    
    @Test
    void testIgnoreNoUser() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(null);
        
        MockHttpServletResponse response = mvc.perform(post("/api/ignore")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new FollowUserRequest("bob")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noUserSet, getErrorSet(response));
    }
    
    
    @Test
    void testIgnoreNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/ignore")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new FollowUserRequest("bob")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testUnfollow() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(bob);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/followings/bob").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).unfollow(alice, bob);
    }
    
    
    @Test
    void testUnfollowNoUser() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(null);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/followings/bob").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noUserSet, getErrorSet(response));
    }
    
    
    @Test
    void testUnfollowNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/followings/bob")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testUnignore() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(bob);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/ignore/bob").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).unignore(alice, bob);
    }
    
    
    @Test
    void testUnignoreNoUser() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(alice);
        when(userDao.getByLogin(anyString())).thenReturn(null);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/ignore/bob").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noUserSet, getErrorSet(response));
    }
    
    
    @Test
    void testUnignoreNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/ignore/bob")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
}
