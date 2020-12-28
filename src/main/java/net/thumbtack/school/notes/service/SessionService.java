package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.LoginRequest;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Service
public class SessionService extends ServiceBase {
    public SessionService(Properties properties, UserDao userDao, SessionDao sessionDao) {
        super(properties, userDao, null, sessionDao);
    }
    
    
    @Scheduled(fixedRateString = "${clear_outdated_sessions_rate}")
    public void clearOutdatedSessions() {
        sessionDao.deleteOutdated();
    }
    
    
    public EmptyResponse login(LoginRequest request, HttpServletResponse response) throws ServerException {
        User user = userDao.getByLogin(request.getLogin());
        
        if (user == null)
            throw new ServerException(ErrorCodeWithField.USER_NOT_FOUND_BY_LOGIN);
        
        if (user.isDeleted())
            throw new ServerException(ErrorCodeWithField.USER_DELETED);
        
        if (!user.getPassword().equals(request.getPassword()))
            throw new ServerException(ErrorCodeWithField.WRONG_PASSWORD);
        
        String token = UUID.randomUUID().toString();
        sessionDao.insert(user, token);
        
        setTokenCookie(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public EmptyResponse logout(String token, HttpServletResponse response) throws ServerException {
        updateSession(response, token, 0);
        
        return new EmptyResponse();
    }
}
