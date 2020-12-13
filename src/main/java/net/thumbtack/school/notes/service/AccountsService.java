package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    
    
    public RegisterUserResponse register(RegisterUserRequest request, HttpServletResponse response) {
        User user = new User(
                request.getLogin(),
                request.getPassword(),
                request.getFirstName(),
                request.getPatronymic(),
                request.getLastName(),
                UserType.USER
        );
        
        String token = UUID.randomUUID().toString();
        userDao.insertAndLogin(user, token);
        
        Cookie cookie = new Cookie("JAVASESSIONID", token);
        cookie.setMaxAge(properties.getUserIdleTimeout());
        response.addCookie(cookie);
        
        return new RegisterUserResponse(
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getLogin()
        );
    }
    
    
    public EmptyResponse deregister(DeregisterUserRequest request, String token, HttpServletResponse response) {
        User user = sessionDao.getUserByToken(token);

//        if (user == null)
//            throw new ServerException();
//
//        if (!user.getPassword().equals(request.getPassword()))
//            throw new ServerException();
        
        userDao.delete(user);
        
        Cookie cookie = new Cookie("JAVASESSIONID", token);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new EmptyResponse();
    }
}
