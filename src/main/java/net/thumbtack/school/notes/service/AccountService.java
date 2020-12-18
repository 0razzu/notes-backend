package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.response.GetCurrentUserResponse;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

// REVU ради одного метода заводить сервис ?
// в AccountsService его, и все дела

@Service
public class AccountService extends BaseService {
    private final SessionDao sessionDao;
    
    
    public AccountService(Properties properties, UserDao userDao, SessionDao sessionDao) {
        super(properties);
        this.sessionDao = sessionDao;
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
}
