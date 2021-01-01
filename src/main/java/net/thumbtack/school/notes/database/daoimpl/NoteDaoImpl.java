package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.NoteDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;


@Repository("noteDao")
public class NoteDaoImpl extends DaoImplBase implements NoteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(NoteDaoImpl.class);
    
    
    @Override
    public void insert(Note note, NoteRevision revision) throws ServerException {
        LOGGER.debug("Inserting {} with {}", note, revision);
        
        try (SqlSession session = getSession()) {
            try {
                getNoteMapper(session).insert(note);
                getNoteRevisionMapper(session).insert(revision, note);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {} with {}", note, revision, e);
                session.rollback();
                if (e.getCause().getClass() == SQLIntegrityConstraintViolationException.class)
                    throw new ServerException(ErrorCodeWithField.SECTION_NOT_FOUND);
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
