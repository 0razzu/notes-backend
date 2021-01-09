package net.thumbtack.school.notes.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.request.UpdateUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.GetCurrentUserResponse;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.dto.response.UpdateUserResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorListResponse;
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

import javax.servlet.http.Cookie;
import java.util.Set;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCode.*;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestAccountsController {
    private static final User USER = new User(0, "eddie", "12345678", "Eddie", null, "Black", UserType.USER);
    private static final User ADMIN = new User(0, "admin", "87654321", "Admin", null, "Admin", UserType.SUPER);
    private static final Cookie COOKIE = new Cookie(JAVA_SESSION_ID, "23ewr23");
    private static final Set<ErrorResponse> NO_COOKIE_SET =
            Set.of(new ErrorResponse("NO_COOKIE", JAVA_SESSION_ID, NO_COOKIE.getMessage()));
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    
    
    private <T> String toJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
    
    
    private Set<ErrorResponse> getErrorSet(MockHttpServletResponse response) throws Exception {
        return Set.copyOf(mapper.readValue(response.getContentAsString(), ErrorListResponse.class).getErrors());
    }
    
    
    @Test
    void testRegister() throws Exception {
        clearInvocations(userDao);
        
        MockHttpServletResponse response = mvc.perform(
                post("/api/accounts")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(toJson(new RegisterUserRequest("Eddie", null, "Black", "eddie", "12345678")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        Cookie cookie = response.getCookie(JAVA_SESSION_ID);
        
        assertAll(
                () -> assertNotNull(cookie),
                () -> assertEquals(new RegisterUserResponse("Eddie", null, "Black", "eddie"),
                        mapper.readValue(response.getContentAsString(), RegisterUserResponse.class))
        );
        
        verify(userDao).insertAndLogin(USER, cookie.getValue());
    }
    
    
    @Test
    void testRegisterConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                post("/api/accounts")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(toJson(new RegisterUserRequest(null, "what", "123", "", "123")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NAME_LENGTH_CONSTRAINT_VIOLATION", "firstName",
                        NAME_LENGTH_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("NAME_CONSTRAINT_VIOLATION", "patronymic", NAME_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("NAME_CONSTRAINT_VIOLATION", "lastName", NAME_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("NAME_LENGTH_CONSTRAINT_VIOLATION", "login",
                        NAME_LENGTH_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("PASSWORD_CONSTRAINT_VIOLATION", "password", PASSWORD_CONSTRAINT_VIOLATION.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testGetCurrentUser() throws Exception {
        clearInvocations(sessionDao);
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(get("/api/account").cookie(COOKIE))
                .andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new GetCurrentUserResponse("Eddie", null, "Black", "eddie"),
                        mapper.readValue(response.getContentAsString(), GetCurrentUserResponse.class))
        );
        
        verify(sessionDao).getUser(COOKIE.getValue());
    }
    
    
    @Test
    void testGetCurrentUserNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/account")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(NO_COOKIE_SET, getErrorSet(response));
    }
    
    
    @Test
    void testDeregister() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/accounts")
                .cookie(COOKIE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new DeregisterUserRequest(USER.getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertEquals(0, response.getCookie(JAVA_SESSION_ID).getMaxAge()),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).delete(USER);
    }
    
    
    @Test
    void testDeregisterUserNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/accounts")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new DeregisterUserRequest(USER.getPassword())))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(NO_COOKIE_SET, getErrorSet(response));
    }
    
    
    @Test
    void testUpdate() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .cookie(COOKIE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest("Ann", null, "White", USER.getPassword(), "zaqwsxcd")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new UpdateUserResponse(0, "Ann", null, "White", USER.getLogin()),
                        mapper.readValue(response.getContentAsString(), UpdateUserResponse.class))
        );
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).update(USER);
    }
    
    
    @Test
    void testUpdateNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest("Ann", null, "White", USER.getPassword(), "zaqwsxcd")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(NO_COOKIE_SET, getErrorSet(response));
    }
    
    
    @Test
    void testUpdateWrongPassword() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .cookie(COOKIE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest("Ann", null, "White", "98765432", "zaqwsxcd")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(new ErrorResponse("WRONG_OLD_PASSWORD", "oldPassword", WRONG_OLD_PASSWORD.getMessage())),
                getErrorSet(response));
    }
    
    
    @Test
    void testUpdateConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .cookie(COOKIE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest(null, null, "", USER.getPassword(), "qqq")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NAME_LENGTH_CONSTRAINT_VIOLATION", "firstName",
                        NAME_LENGTH_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("NAME_LENGTH_CONSTRAINT_VIOLATION", "lastName",
                        NAME_LENGTH_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("PASSWORD_CONSTRAINT_VIOLATION", "newPassword", PASSWORD_CONSTRAINT_VIOLATION.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testMakeSuper() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(ADMIN);
        when(userDao.get(anyInt())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/0/super").cookie(COOKIE)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).update(new User(
                USER.getId(),
                USER.getLogin(),
                USER.getPassword(),
                USER.getFirstName(),
                USER.getPatronymic(),
                USER.getLastName(),
                UserType.SUPER
        ));
    }
    
    
    @Test
    void testMakeSuperByNonSuper() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/0/super").cookie(COOKIE)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NOT_PERMITTED", JAVA_SESSION_ID, NOT_PERMITTED.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testMakeSuperWrongParamType() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/j/super").cookie(COOKIE)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("TYPE_MISMATCH", "id", TYPE_MISMATCH.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testMakeSuperNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/0/super")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(NO_COOKIE_SET, getErrorSet(response));
    }
    
    
    @Test
    void testGetUsersSortByRating() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).getAllWithRating(null, false, null, null);
    }
    
    
    @Test
    void testGetUsersSortByRatingSorted() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(ADMIN);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
                .param("sortByRating", "asc")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).getAllWithRating("asc", true, null, null);
    }
    
    
    @Test
    void testGetUsersByRatingType() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(ADMIN);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
                .param("type", "lowRating")
                .param("count", "3")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).getAllByRatingType("lowRating", true, null, 3);
    }
    
    
    @Test
    void testGetUsersByType() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(ADMIN);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
                .param("type", "deleted")
                .param("from", "1")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).getAllByType("deleted", null, true, 1, null);
    }
    
    
    @Test
    void testGetUsersByRelationToUser() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(USER);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
                .param("type", "followers")
                .param("from", "2")
                .param("count", "5")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(COOKIE.getValue());
        verify(userDao).getAllByRelationToUser(USER, "followers", null, false, 2, 5);
    }
    
    
    @Test
    void testGetUsersTypeMismatch() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
                .param("count", "a")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("TYPE_MISMATCH", "count", TYPE_MISMATCH.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testGetUsersConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(COOKIE)
                .param("sortByRating", "w")
                .param("type", "aaa")
                .param("from", "-1")
                .param("count", "0")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("SORTING_CONSTRAINT_VIOLATION", "sortByRating",
                        SORTING_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("USER_LIST_TYPE_CONSTRAINT_VIOLATION", "type",
                        USER_LIST_TYPE_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("MIN_CONSTRAINT_VIOLATION", "from", MIN_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("MIN_CONSTRAINT_VIOLATION", "count", MIN_CONSTRAINT_VIOLATION.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testGetUsersNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .param("type", "followers")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(NO_COOKIE_SET, getErrorSet(response));
    }
}
