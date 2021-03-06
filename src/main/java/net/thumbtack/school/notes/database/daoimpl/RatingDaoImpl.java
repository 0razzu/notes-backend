package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.RatingDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.Rating;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


@Repository("ratingDao")
public class RatingDaoImpl extends DaoImplBase implements RatingDao {
    private final Logger LOGGER = LoggerFactory.getLogger(RatingDaoImpl.class);
    
    
    @Override
    public void insert(Rating rating, Note note) throws ServerException {
        LOGGER.debug("Inserting {} for {}", rating, note);
        
        try (SqlSession session = getSession()) {
            try {
                getRatingMapper(session).insert(rating, note);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {} for {}", rating, note, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
    
            session.commit();
        }
    }
    
    
    @Override
    public Rating get(Note note, User author) throws ServerException {
        LOGGER.debug("Getting mark for {} by {}", note, author);
        
        try (SqlSession session = getSession()) {
            return getRatingMapper(session).getByNoteAndAuthor(note, author);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get mark for {} by {}", note, author, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
}
