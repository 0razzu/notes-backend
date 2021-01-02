package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@Service
public class ServiceBase {
    protected final Properties properties;
    protected final CommentDao commentDao;
    protected final NoteDao noteDao;
    protected final NoteRevisionDao noteRevisionDao;
    protected final SectionDao sectionDao;
    protected final SessionDao sessionDao;
    protected final UserDao userDao;
    
    
    protected ServiceBase(Properties properties, CommentDao commentDao,
                          NoteDao noteDao, NoteRevisionDao noteRevisionDao, SectionDao sectionDao,
                          SessionDao sessionDao, UserDao userDao) {
        this.properties = properties;
        this.commentDao = commentDao;
        this.noteDao = noteDao;
        this.noteRevisionDao = noteRevisionDao;
        this.sectionDao = sectionDao;
        this.sessionDao = sessionDao;
        this.userDao = userDao;
    }
    
    
    protected void setTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = new Cookie(JAVA_SESSION_ID, token);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/api");
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
