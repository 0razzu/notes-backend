package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.LoginRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.SessionService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api/sessions")
public class SessionsController {
    private final SessionService sessionService;
    
    
    public SessionsController(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse login(@Validated @RequestBody LoginRequest request,
                               HttpServletResponse response) throws ServerException {
        return sessionService.login(request, response);
    }
    
    
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse logout(@CookieValue(value = JAVA_SESSION_ID) String token,
                                HttpServletResponse response) throws ServerException {
        return sessionService.logout(token, response);
    }
}
