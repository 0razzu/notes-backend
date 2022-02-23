package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.util.Properties;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository("sessionDao")
public class SessionDaoImpl extends DaoImplBase implements SessionDao {
    private final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);
    private final Properties properties;
    
    
    public SessionDaoImpl(Properties properties) {
        this.properties = properties;
    }
    
    
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
    public void update(String token) throws ServerException {
        LOGGER.debug("Updating session with token {}", token);
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).update(token);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot update session with token {}", token, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public User getUser(String token) throws ServerException {
        LOGGER.debug("Getting user by token {}", token);
        
        try (SqlSession session = getSession()) {
            return getSessionMapper(session).getUser(token, properties.getUserIdleTimeout());
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get user by token {}", token, e);
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
    public void deleteOutdated() {
        LOGGER.debug("Deleting outdated sessions");
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).deleteOutdated(properties.getUserIdleTimeout());
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete outdated sessions", e);
                session.rollback();
            }
            
            session.commit();
        }
    }
}
