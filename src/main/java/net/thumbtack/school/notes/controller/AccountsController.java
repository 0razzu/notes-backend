package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.service.AccountsService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/accounts")
public class AccountsController {
    private final AccountsService accountsService;
    
    
    public AccountsController(AccountsService accountsService, Environment environment) {
        this.accountsService = accountsService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterUserResponse> register(@Validated @RequestBody RegisterUserRequest request) {
        return accountsService.register(request);
    }
}
