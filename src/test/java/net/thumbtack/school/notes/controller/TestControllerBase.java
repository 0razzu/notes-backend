package net.thumbtack.school.notes.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.notes.dto.response.error.ErrorListResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import java.util.Set;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.NO_COOKIE;


public class TestControllerBase {
    @Autowired
    protected ObjectMapper mapper;
    
    protected static final Cookie cookie = new Cookie(JAVA_SESSION_ID, "23ewr23");
    protected static final Set<ErrorResponse> noCookieSet =
            Set.of(new ErrorResponse("NO_COOKIE", JAVA_SESSION_ID, NO_COOKIE.getMessage()));
    protected static final User admin = new User("admin", "87654321", "Admin", null, "Admin", UserType.SUPER);
    
    
    protected <T> String toJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
    
    
    protected Set<ErrorResponse> getErrorSet(MockHttpServletResponse response) throws Exception {
        return Set.copyOf(mapper.readValue(response.getContentAsString(), ErrorListResponse.class).getErrors());
    }
}
