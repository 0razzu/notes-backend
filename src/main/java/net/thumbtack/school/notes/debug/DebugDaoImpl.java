package net.thumbtack.school.notes.debug;


import net.thumbtack.school.notes.database.daoimpl.DaoImplBase;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository("debugDao")
public class DebugDaoImpl extends DaoImplBase implements DebugDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugDaoImpl.class);
    
    
    @Override
    public void clear() throws ServerException {
        LOGGER.debug("Clearing database");
        
        try (SqlSession session = getSession()) {
            try {
                DebugMapper debugMapper = session.getMapper(DebugMapper.class);
                debugMapper.deleteRatings();
                debugMapper.deleteComments();
                debugMapper.deleteNotes();
                debugMapper.deleteSections();
                debugMapper.deleteSessions();
                debugMapper.deleteUsers();
            } catch (RuntimeException e) {
                LOGGER.info("Cannot clear database", e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
