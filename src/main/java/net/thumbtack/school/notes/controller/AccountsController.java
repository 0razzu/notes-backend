package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@Controller
@RequestMapping("/api/accounts")
public class AccountsController {
	// REVU контроллер не имеет доступа к DAO. Это в сервис
    private final UserDao userDao;
    private final SessionDao sessionDao;
    
    
    public AccountsController(UserDao userDao, SessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterUserResponse> register(@Validated @RequestBody RegisterUserRequest request) { // TODO: exception handling
        // REVU это все в сервис. Контроллер только вызывает метод сервиса
        User user = new User(
                request.getLogin(),
                request.getPassword(),
                request.getFirstName(),
                request.getPatronymic(),
                request.getLastName(),
                UserType.USER
        );
        userDao.insert(user);
        String token = UUID.randomUUID().toString();
        sessionDao.insert(user, token);
        
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, "JAVASESSIONID = " + token).body(
                new RegisterUserResponse(
                        user.getFirstName(),
                        user.getPatronymic(),
                        user.getLastName(),
                        user.getLogin()
                )
        );
    }
}
