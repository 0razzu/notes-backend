package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@Service
public class ServiceBase {
    protected final Properties properties;
    protected final UserDao userDao;
    protected final SessionDao sessionDao;
    
    
    protected ServiceBase(Properties properties, UserDao userDao, SessionDao sessionDao) {
        this.properties = properties;
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }
    
    
    protected void setTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = new Cookie(JAVA_SESSION_ID, token);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    
    protected void updateSession(HttpServletResponse response, String token, int maxAge) throws ServerException {
        if (maxAge == 0)
            sessionDao.delete(token);
        else
            sessionDao.update(token);
        
        setTokenCookie(response, token, maxAge);
    }
    
    
    protected User getUserByToken(String token) throws ServerException {
        User user = sessionDao.getUserByToken(token);
        
        if (user == null)
            throw new ServerException(ErrorCodeWithField.SESSION_NOT_FOUND);
        
        return user;
    }
}
