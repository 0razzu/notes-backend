package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.NoteRevisionDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("noteRevisionDao")
public class NoteRevisionDaoImpl extends DaoImplBase implements NoteRevisionDao {
    private final Logger LOGGER = LoggerFactory.getLogger(NoteRevisionDaoImpl.class);
    
    
    @Override
    public List<NoteRevision> getByNote(Note note) throws ServerException {
        LOGGER.debug("Getting revisions of {}", note);
        
        try (SqlSession session = getSession()) {
            return getNoteRevisionMapper(session).getByNote(note);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get revisions of {}", note, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public NoteRevision getMostRecent(Note note) throws ServerException {
        LOGGER.debug("Getting most recent revision of {}", note);
        
        try (SqlSession session = getSession()) {
            return getNoteRevisionMapper(session).getMostRecent(note);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get most recent revision of {}", note, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
}
