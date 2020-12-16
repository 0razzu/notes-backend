package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.request.UpdateUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.GetCurrentUserResponse;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.dto.response.UpdateUserResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.AccountsService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    private final AccountsService accountsService;
    
    
    public AccountsController(AccountsService accountsService, Environment environment) {
        this.accountsService = accountsService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegisterUserResponse register(@Validated @RequestBody RegisterUserRequest request,
                                         HttpServletResponse response) throws ServerException {
        return accountsService.register(request, response);
    }
    
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GetCurrentUserResponse getCurrentUser(@CookieValue(value = JAVA_SESSION_ID) String token,
                                                 HttpServletResponse response)
            throws ServerException {
        return accountsService.getCurrentUser(token, response);
    }
    
    
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse deregister(@Validated @RequestBody DeregisterUserRequest request,
                                    @CookieValue(value = JAVA_SESSION_ID) String token,
                                    HttpServletResponse response) throws ServerException {
        return accountsService.deregister(request, token, response);
    }
    
    
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UpdateUserResponse update(@Validated @RequestBody UpdateUserRequest request,
                                     @CookieValue(value = JAVA_SESSION_ID) String token,
                                     HttpServletResponse response) throws ServerException {
        return accountsService.update(request, token, response);
    }
    
    
    @PutMapping(path = "/{id}/super",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse makeSuper(@PathVariable("id") int id,
                                   @CookieValue(value = JAVA_SESSION_ID) String token,
                                   HttpServletResponse response) throws ServerException {
        return accountsService.makeSuper(id, token, response);
    }
}
