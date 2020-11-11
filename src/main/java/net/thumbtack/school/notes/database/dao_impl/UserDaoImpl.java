package net.thumbtack.school.notes.database.dao_impl;


import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserDaoImpl extends DaoImplBase implements UserDao {
    private final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    
    
    @Override
    public void insert(User user) {
        LOGGER.debug("Inserting {}", user);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).insert(user);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {}", user, e);
                session.rollback();
                throw e;
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void update(User user) {
        LOGGER.debug("Updating {}", user);
        
        try (SqlSession session = getSession()) {
            try {
                getUserMapper(session).update(user);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot update {}", user, e);
                session.rollback();
                throw e;
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public User get(int id) {
        LOGGER.debug("Getting user by id {}", id);
        
        try (SqlSession session = getSession()) {
            return getUserMapper(session).get(id);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get user by id {}", id, e);
            throw e;
        }
    }
}
