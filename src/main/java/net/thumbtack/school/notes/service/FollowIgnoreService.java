package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.FollowUserRequest;
import net.thumbtack.school.notes.dto.request.IgnoreUserRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;


@Service
public class FollowIgnoreService extends ServiceBase {
    public FollowIgnoreService(Properties properties, SessionDao sessionDao, UserDao userDao) {
        super(properties, null, null, null, null, sessionDao, userDao);
    }
    
    
    public EmptyResponse follow(FollowUserRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        User followed = userDao.getByLogin(request.getLogin());
        
        if (followed == null)
            throw new ServerException(ErrorCodeWithField.USER_NOT_FOUND_BY_LOGIN);
        
        userDao.follow(user, followed);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public EmptyResponse ignore(IgnoreUserRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        User ignored = userDao.getByLogin(request.getLogin());
        
        if (ignored == null)
            throw new ServerException(ErrorCodeWithField.USER_NOT_FOUND_BY_LOGIN);
        
        userDao.ignore(user, ignored);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public EmptyResponse unfollow(String login, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        User followed = userDao.getByLogin(login);
        
        if (followed == null)
            throw new ServerException(ErrorCodeWithField.USER_NOT_FOUND_BY_LOGIN);
        
        userDao.unfollow(user, followed);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public EmptyResponse unignore(String login, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        User ignored = userDao.getByLogin(login);
        
        if (ignored == null)
            throw new ServerException(ErrorCodeWithField.USER_NOT_FOUND_BY_LOGIN);
        
        userDao.unignore(user, ignored);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
}
