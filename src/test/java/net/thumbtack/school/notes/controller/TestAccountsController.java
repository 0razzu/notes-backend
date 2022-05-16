package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.request.UpdateUserRequest;
import net.thumbtack.school.notes.dto.response.*;
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

import static net.thumbtack.school.notes.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCode.*;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.WRONG_OLD_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TestAccountsController extends TestControllerBase {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private UserDao userDao;
    
    private static final User user = new User("eddie", "12345678", "Eddie", null, "Black", UserType.USER);
    
    
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
        
        verify(userDao).insertAndLogin(user, cookie.getValue());
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
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(get("/api/account").cookie(cookie))
                .andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new GetCurrentUserResponse(0, "Eddie", null, "Black", "eddie", null),
                        mapper.readValue(response.getContentAsString(), GetCurrentUserResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testGetCurrentUserNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/account")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetUser() throws Exception {
        clearInvocations(sessionDao);
        when(sessionDao.getUser(anyString())).thenReturn(user);
        when(userDao.getByLogin("admin")).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts/admin").cookie(cookie))
                .andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new GetUserResponse(0, "Admin", null, "Admin", null),
                        mapper.readValue(response.getContentAsString(), GetUserResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
    }
    
    
    @Test
    void testGetUserNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/accounts/admin")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testDeregister() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(delete("/api/accounts")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new DeregisterUserRequest(user.getPassword())))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertEquals(0, response.getCookie(JAVA_SESSION_ID).getMaxAge()),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).delete(user);
    }
    
    
    @Test
    void testDeregisterUserNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(delete("/api/accounts")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new DeregisterUserRequest(user.getPassword())))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testUpdate() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest("Ann", null, "White", user.getPassword(), "zaqwsxcd")))
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new UpdateUserResponse(0, "Ann", null, "White", user.getLogin()),
                        mapper.readValue(response.getContentAsString(), UpdateUserResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).update(user);
    }
    
    
    @Test
    void testUpdateNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest("Ann", null, "White", user.getPassword(), "zaqwsxcd")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testUpdateWrongPassword() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest("Ann", null, "White", "98765432", "zaqwsxcd")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(new ErrorResponse("WRONG_OLD_PASSWORD", "oldPassword", WRONG_OLD_PASSWORD.getMessage())),
                getErrorSet(response));
    }
    
    
    @Test
    void testUpdateConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts")
                .cookie(cookie)
                .contentType(APPLICATION_JSON_VALUE)
                .content(toJson(new UpdateUserRequest(null, null, "", user.getPassword(), "qqq")))
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("NAME_LENGTH_CONSTRAINT_VIOLATION", "firstName",
                        NAME_LENGTH_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("NAME_LENGTH_CONSTRAINT_VIOLATION", "lastName",
                        NAME_LENGTH_CONSTRAINT_VIOLATION.getMessage()),
                new ErrorResponse("PASSWORD_CONSTRAINT_VIOLATION", "newPassword",
                        PASSWORD_CONSTRAINT_VIOLATION.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testMakeSuper() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        when(userDao.get(anyInt())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/0/super").cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertAll(
                () -> assertNotNull(response.getCookie(JAVA_SESSION_ID)),
                () -> assertEquals(new EmptyResponse(),
                        mapper.readValue(response.getContentAsString(), EmptyResponse.class))
        );
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).update(new User(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                UserType.SUPER
        ));
    }
    
    
    @Test
    void testMakeSuperByNonSuper() throws Exception {
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/0/super").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(notPermittedSet, getErrorSet(response));
    }
    
    
    @Test
    void testMakeSuperWrongParamType() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/j/super").cookie(cookie)
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("TYPE_MISMATCH", "id", TYPE_MISMATCH.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testMakeSuperNoCookie() throws Exception {
        MockHttpServletResponse response = mvc.perform(put("/api/accounts/0/super")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
    
    
    @Test
    void testGetUsersSortByRating() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).getAllWithRating(null, false, null, null);
    }
    
    
    @Test
    void testGetUsersSortByRatingSorted() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
                .param("sortByRating", "asc")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).getAllWithRating("asc", true, null, null);
    }
    
    
    @Test
    void testGetUsersByRatingType() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
                .param("type", "lowRating")
                .param("count", "3")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).getAllByRatingType("lowRating", true, null, 3);
    }
    
    
    @Test
    void testGetUsersByType() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(admin);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
                .param("type", "deleted")
                .param("from", "1")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).getAllByType("deleted", null, true, 1, null);
    }
    
    
    @Test
    void testGetUsersByRelationToUser() throws Exception {
        clearInvocations(sessionDao);
        clearInvocations(userDao);
        when(sessionDao.getUser(anyString())).thenReturn(user);
        
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
                .param("type", "followers")
                .param("from", "2")
                .param("count", "5")
        ).andExpect(status().isOk()).andReturn().getResponse();
        
        assertNotNull(response.getCookie(JAVA_SESSION_ID));
        
        verify(sessionDao).getUser(cookie.getValue());
        verify(userDao).getAllByRelationToUser(user, "followers", null, false, 2, 5);
    }
    
    
    @Test
    void testGetUsersTypeMismatch() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
                .param("count", "a")
        ).andExpect(status().isBadRequest()).andReturn().getResponse();
        
        assertEquals(Set.of(
                new ErrorResponse("TYPE_MISMATCH", "count", TYPE_MISMATCH.getMessage())
        ), getErrorSet(response));
    }
    
    
    @Test
    void testGetUsersConstraints() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/api/accounts")
                .cookie(cookie)
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
        
        assertEquals(noCookieSet, getErrorSet(response));
    }
}
