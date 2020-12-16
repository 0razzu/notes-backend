package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.request.UpdateUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.dto.response.GetCurrentUserResponse;
import net.thumbtack.school.notes.dto.response.RegisterUserResponse;
import net.thumbtack.school.notes.dto.response.UpdateUserResponse;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


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
    
    
    private void setTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = new Cookie(JAVA_SESSION_ID, token);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    
    public RegisterUserResponse register(RegisterUserRequest request, HttpServletResponse response)
            throws ServerException {
        User user = new User(
                request.getLogin(),
                request.getPassword(),
                request.getFirstName(),
                request.getPatronymic(),
                request.getLastName(),
                UserType.USER
        );
        
        String token = UUID.randomUUID().toString();
        try {
            userDao.insertAndLogin(user, token);
        } catch (PersistenceException e) {
            if (e.getCause().getClass() == SQLIntegrityConstraintViolationException.class)
                throw new ServerException(ErrorCodeWithField.LOGIN_EXISTS);
            throw e;
        }
    
        setTokenCookie(response, token, properties.getUserIdleTimeout());
        return new RegisterUserResponse(
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getLogin()
        );
    }
    
    
    public GetCurrentUserResponse getCurrentUser(String token, HttpServletResponse response)
            throws ServerException {
        User user = sessionDao.getUserByToken(token);
        
        if (user == null)
            throw new ServerException(ErrorCodeWithField.SESSION_NOT_FOUND);
        
        setTokenCookie(response, token, properties.getUserIdleTimeout());
        return new GetCurrentUserResponse(
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getLogin()
        );
    }
    
    
    public EmptyResponse deregister(DeregisterUserRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = sessionDao.getUserByToken(token);
        
        if (user == null)
            throw new ServerException(ErrorCodeWithField.SESSION_NOT_FOUND);
        
        if (!user.getPassword().equals(request.getPassword()))
            throw new ServerException(ErrorCodeWithField.WRONG_PASSWORD);
        
        sessionDao.delete(user);
        userDao.update(user);
        
        setTokenCookie(response, token, 0);
        return new EmptyResponse();
    }
    
    
    public UpdateUserResponse update(UpdateUserRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = sessionDao.getUserByToken(token);
        
        if (user == null)
            throw new ServerException(ErrorCodeWithField.SESSION_NOT_FOUND);
        
        if (!user.getPassword().equals(request.getOldPassword()))
            throw new ServerException(ErrorCodeWithField.WRONG_OLD_PASSWORD);
        
        user.setFirstName(request.getFirstName());
        user.setPatronymic(request.getPatronymic());
        user.setLastName(request.getLastName());
        user.setPassword(request.getNewPassword());
        userDao.update(user);
        
        setTokenCookie(response, token, properties.getUserIdleTimeout());
        return new UpdateUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getPatronymic(),
                user.getLastName(),
                user.getLogin()
        );
    }
}
