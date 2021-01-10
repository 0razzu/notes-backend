package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.dto.request.LoginRequest;
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
import static net.thumbtack.school.notes.error.ErrorCode.NOT_BLANK;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestSessionsController extends TestControllerBase {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    
    private static final User user = new User("sammy", "qwerty09", "Sam", null, "Wood", UserType.USER);
    private static final User deleted = new User("sammy", "qwerty09", "Sam", null, "Wood", UserType.USER, true);
    
    
    @Test
    void testLogin() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(userDao.getByLogin(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(post("/api/sessions")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new LoginRequest("sammy", "qwerty09")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).insert(any(), anyString());
        verify(userDao).getByLogin("sammy");
    }
    
    
    @Test
    void testLoginConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/api/sessions")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new LoginRequest("", null)))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_BLANK", "login", NOT_BLANK.getMessage()),
                new ErrorResponse("NOT_BLANK", "password", NOT_BLANK.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testLoginNoUser() throws Exception {
        when(userDao.getByLogin(anyString())).thenReturn(null);
        
        MockHttpServletResponse response = mvc.perform(post("/api/sessions")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new LoginRequest("sammy", "qwerty09")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("USER_NOT_FOUND_BY_LOGIN", "login", USER_NOT_FOUND_BY_LOGIN.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testLoginDeleted() throws Exception {
        when(userDao.getByLogin(anyString())).thenReturn(deleted);
        
        MockHttpServletResponse response = mvc.perform(post("/api/sessions")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new LoginRequest("sammy", "qwerty09")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("USER_DELETED", "login", USER_DELETED.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testLoginWrongPassword() throws Exception {
        when(userDao.getByLogin(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(post("/api/sessions")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new LoginRequest("sammy", "12345678")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("WRONG_PASSWORD", "password", WRONG_PASSWORD.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testLogout() throws Exception {
        clearInvocations(sessionDao);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/sessions").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertEquals(0, response.getCookie(JAVA_SESSION_ID).getMaxAge()),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).delete(cookie.getValue());
    }
    
    
    @Test
    void testLogoutNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/sessions")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
}
