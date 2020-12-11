package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Service
public class AccountsService {
    private final Properties properties;
    private final UserDao userDao;
    private final SessionDao sessionDao;
    
    
    public AccountsService(Properties properties, UserDao userDao, SessionDao sessionDao) {
        this.properties = properties;
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }
    
    
    public ResponseEntity<RegisterUserResponse> register(RegisterUserRequest request) {
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
        
        HttpCookie cookie = ResponseCookie.from("JAVASESSIONID", token)
                .path("/")
                .maxAge(Duration.of(properties.getUserIdleTimeout(), ChronoUnit.SECONDS))
                .httpOnly(true)
                .build();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(
                        new RegisterUserResponse(
                                user.getFirstName(),
                                user.getPatronymic(),
                                user.getLastName(),
                                user.getLogin()
                        )
                );
    }
}
