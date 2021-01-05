package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.NoteDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.view.NoteView;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("noteDao")
public class NoteDaoImpl extends DaoImplBase implements NoteDao {
    private final Logger LOGGER = LoggerFactory.getLogger(NoteDaoImpl.class);
    
    
    @Override
    public void insert(Note note, NoteRevision revision) throws ServerException {
        LOGGER.debug("Inserting {} with {}", note, revision);
        
        try (SqlSession session = getSession()) {
            try {
                getNoteMapper(session).insert(note);
                getNoteRevisionMapper(session).insert(revision);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {} with {}", note, revision, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void update(Note note, NoteRevision revision) throws ServerException {
        LOGGER.debug("Updating {} with {}", note, revision);
        
        try (SqlSession session = getSession()) {
            try {
                if (note != null)
                    getNoteMapper(session).update(note);
                if (revision != null)
                    getNoteRevisionMapper(session).insert(revision);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot update {} with {}", note, revision, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public Note get(int id) throws ServerException {
        LOGGER.debug("Getting note by id {}", id);
        
        try (SqlSession session = getSession()) {
            return getNoteMapper(session).get(id);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get note by id {}", id, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public NoteView getView(int id) throws ServerException {
        LOGGER.debug("Getting note view by id {}", id);
        
        try (SqlSession session = getSession()) {
            return getNoteMapper(session).getView(id);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get note view by id {}", id, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<NoteView> getAllByParams(Integer sectionId,
                                         String tags,
                                         Integer author, int user, String include,
                                         boolean comments, boolean allVersions, boolean commentVersion,
                                         Integer from, Integer count) throws ServerException {
        LOGGER.debug("Getting note views by params");
        
        try (SqlSession session = getSession()) {
            Map<String, Object> params = new HashMap<>();
            params.put("sectionId", sectionId);
            params.put("tags", tags);
            params.put("author", author);
            params.put("user", user);
            params.put("include", include);
            params.put("comments", comments);
            params.put("allVersions", allVersions);
            params.put("commentVersion", commentVersion);
            params.put("from", from);
            params.put("count", count);
            
            return session.selectList("net.thumbtack.school.notes.database.mapper.NoteMapper.getAllByParams",
                    params);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get note views by params", e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public void delete(Note note) throws ServerException {
        LOGGER.debug("Deleting {}", note);
        
        try (SqlSession session = getSession()) {
            try {
                getNoteMapper(session).delete(note);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete {}", note, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
