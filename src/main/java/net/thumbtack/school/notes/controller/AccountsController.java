package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.service.AccountsService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    private final AccountsService accountsService;
    
    
    public AccountsController(AccountsService accountsService, Environment environment) {
        this.accountsService = accountsService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RegisterUserResponse register(@Validated @RequestBody RegisterUserRequest request,
                                         HttpServletResponse response) {
        return accountsService.register(request, response);
    }
    
    
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse deregister(@Validated @RequestBody DeregisterUserRequest request,
                                    @CookieValue(value = "JAVASESSIONID") String token,
                                    HttpServletResponse response) {
        return accountsService.deregister(request, token, response);
    }
}
