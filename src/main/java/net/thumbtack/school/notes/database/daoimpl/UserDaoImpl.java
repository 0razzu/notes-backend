package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.view.UserView;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


@Repository("userDao")
public class UserDaoImpl extends DaoImplBase implements UserDao {
    private final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    
    
    @Override
    public void insert(User user) throws ServerException {
        LOGGER.debug("Inserting {}", user);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).insert(user);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {}", user, e);
                session.rollback();
                if (e.getCause().getClass() == SQLIntegrityConstraintViolationException.class)
                    throw new ServerException(ErrorCodeWithField.LOGIN_EXISTS);
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void insertAndLogin(User user, String token) throws ServerException {
        LOGGER.debug("Inserting and logging in {} with token {}", user, token);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).insert(user);
                getSessionMapper(session).insert(user, token);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {} with token {}", user, token, e);
                session.rollback();
                if (e.getCause().getClass() == SQLIntegrityConstraintViolationException.class)
                    throw new ServerException(ErrorCodeWithField.LOGIN_EXISTS);
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void update(User user) throws ServerException {
        LOGGER.debug("Updating {}", user);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).update(user);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot update {}", user, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void follow(User user, User followed) throws ServerException {
        LOGGER.debug("Making {} follow {}", user, followed);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).follow(user, followed);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot make {} follow {}", user, followed, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void unfollow(User user, User followed) throws ServerException {
        LOGGER.debug("Making {} unfollow {}", user, followed);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).unfollow(user, followed);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot make {} unfollow {}", user, followed, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void ignore(User user, User ignored) throws ServerException {
        LOGGER.debug("Making {} ignore {}", user, ignored);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).ignore(user, ignored);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot make {} ignore {}", user, ignored, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void unignore(User user, User ignored) throws ServerException {
        LOGGER.debug("Making {} unignore {}", user, ignored);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).unignore(user, ignored);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot make {} unignore {}", user, ignored, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public User get(int id) throws ServerException {
        LOGGER.debug("Getting user by id {}", id);
        
        try (SqlSession session = getSession()) {
            return getUserMapper(session).get(id);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get user by id {}", id, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<User> getAll() throws ServerException {
        LOGGER.debug("Getting all users");
        
        try (SqlSession session = getSession()) {
            return getUserMapper(session).getAll();
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get all users", e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<UserView> getAllWithRating(String sortByRating, boolean selectSuper, Integer from, Integer count) throws ServerException {
        LOGGER.debug("Getting all users with rating (sortByRating={}, selectSuper={}, from={}, count={})",
                sortByRating, selectSuper, from, count);
        
        try (SqlSession session = getSession()) {
            return getUserMapper(session).getAllWithRating(sortByRating, selectSuper, from, count);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get all users with rating (sortByRating={}, selectSuper={}, from={}, count={})",
                    sortByRating, selectSuper, from, count, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<UserView> getAllByType(String userType, String sortByRating, boolean selectSuper, Integer from, Integer count) throws ServerException {
        LOGGER.debug("Getting all users by type {} (sortByRating={}, selectSuper={}, from={}, count={})",
                userType, sortByRating, selectSuper, from, count);
        
        try (SqlSession session = getSession()) {
            return getUserMapper(session).getAllByType(userType, sortByRating, selectSuper, from, count);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get all users by type {} (sortByRating={}, selectSuper={}, from={}, count={})",
                    userType, sortByRating, selectSuper, from, count, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<UserView> getAllByRelationToUser(User user, String relation, String sortByRating, boolean selectSuper, Integer from, Integer count) throws ServerException {
        LOGGER.debug("Getting all users by relation {} to {} (sortByRating={}, selectSuper={}, from={}, count={})",
                relation, user, sortByRating, selectSuper, from, count);
        
        try (SqlSession session = getSession()) {
            return getUserMapper(session).getAllByRelationToUser(user, relation, sortByRating, selectSuper, from, count);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get all users by relation {} to {} (sortByRating={}, selectSuper={}, from={}, count={}",
                    relation, user, sortByRating, selectSuper, from, count, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public void delete(User user) throws ServerException {
        LOGGER.debug("Deleting {}", user);
        
        try (SqlSession session = getSession()) {
            try {
                getSessionMapper(session).deleteByUser(user);
                user.setDeleted(true);
                getUserMapper(session).update(user);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete {}", user, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void deleteAll() throws ServerException {
        LOGGER.debug("Deleting all users");
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).deleteAll();
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete all users", e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
