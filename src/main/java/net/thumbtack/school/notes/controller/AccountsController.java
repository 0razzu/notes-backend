package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.GetUsersRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.request.UpdateUserRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api")
public class AccountsController {
    private final AccountService accountService;
    
    
    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    
    @PostMapping(path = "/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegisterUserResponse register(@Validated @RequestBody RegisterUserRequest request,
                                         HttpServletResponse response) throws ServerException {
        return accountService.register(request, response);
    }
    
    
    @GetMapping(path = "/account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetCurrentUserResponse getCurrentUser(@CookieValue(value = JAVA_SESSION_ID) String token,
                                                 HttpServletResponse response)
            throws ServerException {
        return accountService.getCurrentUser(token, response);
    }
    
    
    @DeleteMapping(path = "/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse deregister(@Validated @RequestBody DeregisterUserRequest request,
                                    @CookieValue(value = JAVA_SESSION_ID) String token,
                                    HttpServletResponse response) throws ServerException {
        return accountService.deregister(request, token, response);
    }
    
    
    @PutMapping(path = "/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UpdateUserResponse update(@Validated @RequestBody UpdateUserRequest request,
                                     @CookieValue(value = JAVA_SESSION_ID) String token,
                                     HttpServletResponse response) throws ServerException {
        return accountService.update(request, token, response);
    }
    
    
    @PutMapping(path = "/accounts/{id}/super",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse makeSuper(@PathVariable("id") int id,
                                   @CookieValue(value = JAVA_SESSION_ID) String token,
                                   HttpServletResponse response) throws ServerException {
        return accountService.makeSuper(id, token, response);
    }
    
    
    @GetMapping(path = "/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<GetUsersResponseItem> getUsers(@Validated @RequestBody GetUsersRequest request,
                                               @CookieValue(value = JAVA_SESSION_ID) String token,
                                               HttpServletResponse response) throws ServerException {
        return accountService.getUsers(request, token, response);
    }
}
