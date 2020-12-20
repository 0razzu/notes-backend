package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.CommonDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository("commonDao")
public class CommonDaoImpl extends DaoImplBase implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);
    
    
    @Override
    public void clear() throws ServerException {
        LOGGER.debug("Clearing database");
        
        try (SqlSession session = getSession()) {
            try {
//                getCommentMapper(session).deleteAll();
//                getNoteMapper(session).deleteAll();
//                getRatingMapper(session).deleteAll();
//                getSectionMapper(session).deleteAll();
                getSessionMapper(session).deleteAll();
                getUserMapper(session).deleteAll();
            } catch (RuntimeException e) {
                LOGGER.info("Cannot clear database", e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
