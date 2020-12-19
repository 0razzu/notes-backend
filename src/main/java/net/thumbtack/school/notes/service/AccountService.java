package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.DeregisterUserRequest;
import net.thumbtack.school.notes.dto.request.GetUsersRequest;
import net.thumbtack.school.notes.dto.request.RegisterUserRequest;
import net.thumbtack.school.notes.dto.request.UpdateUserRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import net.thumbtack.school.notes.view.UserView;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AccountService extends ServiceBase {
    public AccountService(Properties properties, UserDao userDao, SessionDao sessionDao) {
        super(properties, userDao, sessionDao);
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
        userDao.insertAndLogin(user, token);
        
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
        User user = getUserByToken(token);
        
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
        User user = getUserByToken(token);
        
        if (!user.getPassword().equals(request.getPassword()))
            throw new ServerException(ErrorCodeWithField.WRONG_PASSWORD);
        
        sessionDao.delete(user);
        userDao.update(user);
        
        setTokenCookie(response, token, 0);
        return new EmptyResponse();
    }
    
    
    public UpdateUserResponse update(UpdateUserRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        
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
    
    
    public EmptyResponse makeSuper(int id, String token, HttpServletResponse response) throws ServerException {
        User superuser = getUserByToken(token);
        
        if (superuser.getType() != UserType.SUPER)
            throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
        
        User user = userDao.get(id);
        
        if (user == null)
            throw new ServerException(ErrorCodeWithField.USER_NOT_FOUND_BY_ID);
        
        user.setType(UserType.SUPER);
        userDao.update(user);
        
        setTokenCookie(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public List<GetUsersResponseItem> getUsers(GetUsersRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        
        List<UserView> userViews = null;
        
        String type = request.getType();
        boolean isSuper = (user.getType() == UserType.SUPER);
        
        if (type == null || type.equals("super") && !isSuper)
            userViews = userDao.getAllWithRating(
                    request.getSortByRating(),
                    isSuper,
                    request.getFrom(),
                    request.getCount()
            );
        else
            switch (type) {
                case "highRating":
                case "lowRating": {
                    userViews = userDao.getAllByRatingType(
                            type,
                            isSuper,
                            request.getFrom(),
                            request.getCount()
                    );
                    break;
                }
                
                case "following":
                case "followers":
                case "ignore":
                case "ignoredBy": {
                    userViews = userDao.getAllByRelationToUser(
                            user,
                            type,
                            request.getSortByRating(),
                            isSuper,
                            request.getFrom(),
                            request.getCount()
                    );
                    break;
                }
                
                case "super":
                case "deleted": {
                    userViews = userDao.getAllByType(
                            type,
                            request.getSortByRating(),
                            isSuper,
                            request.getFrom(),
                            request.getCount()
                    );
                }
            }
        
        setTokenCookie(response, token, properties.getUserIdleTimeout());
        return userViews.stream().map((UserView u) -> new GetUsersResponseItem(
                u.getId(),
                u.getFirstName(),
                u.getPatronymic(),
                u.getLastName(),
                u.getLogin(),
                u.getTimeRegistered(),
                u.isOnline(),
                u.isDeleted(),
                u.getIsSuper(),
                u.getRating()
        )).collect(Collectors.toList());
    }
}
