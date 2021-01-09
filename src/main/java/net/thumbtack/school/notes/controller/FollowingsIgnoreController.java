package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.FollowUserRequest;
import net.thumbtack.school.notes.dto.request.IgnoreUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.FollowIgnoreService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api")
public class FollowingsIgnoreController {
    private final FollowIgnoreService followIgnoreService;
    
    
    public FollowingsIgnoreController(FollowIgnoreService followIgnoreService) {
        this.followIgnoreService = followIgnoreService;
    }
    
    
    @PostMapping(path = "/followings",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse follow(@Validated @RequestBody FollowUserRequest request,
                                @CookieValue(value = JAVA_SESSION_ID) String token,
                                HttpServletResponse response) throws ServerException {
        return followIgnoreService.follow(request, token, response);
    }
    
    
    @PostMapping(path = "/ignore",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse ignore(@Validated @RequestBody IgnoreUserRequest request,
                                @CookieValue(value = JAVA_SESSION_ID) String token,
                                HttpServletResponse response) throws ServerException {
        return followIgnoreService.ignore(request, token, response);
    }
    
    
    @DeleteMapping(path = "/followings/{login}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse unfollow(@PathVariable("login") String login,
                                  @CookieValue(value = JAVA_SESSION_ID) String token,
                                  HttpServletResponse response) throws ServerException {
        return followIgnoreService.unfollow(login, token, response);
    }
    
    
    @DeleteMapping(path = "/ignore/{login}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse unignore(@PathVariable("login") String login,
                                  @CookieValue(value = JAVA_SESSION_ID) String token,
                                  HttpServletResponse response) throws ServerException {
        return followIgnoreService.unignore(login, token, response);
    }
}
