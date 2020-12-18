package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.response.GetCurrentUserResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.AccountService;
import net.thumbtack.school.notes.service.AccountsService;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;

//REVU ради одного метода заводить контроллер ?
//в AccountsController его, и все дела

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;
    
    
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GetCurrentUserResponse getCurrentUser(@CookieValue(value = JAVA_SESSION_ID) String token,
                                                 HttpServletResponse response)
            throws ServerException {
        return accountService.getCurrentUser(token, response);
    }
}
