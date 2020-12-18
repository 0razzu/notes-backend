package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("sessionDao")
public class SessionDaoImpl extends DaoImplBase implements SessionDao {
    private final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    
    
    @Override
    public void insert(User user, String token) throws ServerException {
        LOGGER.debug("Inserting session of {} with token {}", user, token);
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).insert(user, token);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert session of {} with token {}", user, token, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public User getUserByToken(String token) throws ServerException {
        LOGGER.debug("Getting user by token {}", token);
        
        try (SqlSession session = getSession()) {
            return getSessionMapper(session).getUserByToken(token);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get user by token {}", token, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public boolean isOnline(String token) throws ServerException {
        LOGGER.debug("Checking if user with token {} is online", token);
        
        try (SqlSession session = getSession()) {
            return getSessionMapper(session).isOnlineByToken(token);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot check if user with token {} is online", token, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public boolean isOnline(User user) throws ServerException {
        LOGGER.debug("Checking if {} is online", user);
        
        try (SqlSession session = getSession()) {
            return getSessionMapper(session).isOnlineByUser(user);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot check if {} is online", user, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<User> getOnline() throws ServerException {
        LOGGER.debug("Getting online users");
        
        try (SqlSession session = getSession()) {
            return getSessionMapper(session).getOnline();
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get online users", e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public void delete(String token) throws ServerException {
        LOGGER.debug("Deleting session of user with token {}", token);
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).deleteByToken(token);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete session of user with token {}", token, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void delete(User user) throws ServerException {
        LOGGER.debug("Deleting session of {}", user);
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).deleteByUser(user);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete session of {}", user, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void deleteAll() throws ServerException {
        LOGGER.debug("Deleting all sessions");
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).deleteAll();
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete all sessions", e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
