package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.CommentDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository("commentDao")
public class CommentDaoImpl extends DaoImplBase implements CommentDao {
    private final Logger LOGGER = LoggerFactory.getLogger(CommentDaoImpl.class);
    
    
    @Override
    public void insert(Comment comment) throws ServerException {
        LOGGER.debug("Inserting {}", comment);
        
        try (SqlSession session = getSession()) {
            try {
                getCommentMapper(session).insert(comment);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {}", comment, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void update(Comment comment) throws ServerException {
        LOGGER.debug("Updating {}", comment);
        
        try (SqlSession session = getSession()) {
            try {
                getCommentMapper(session).update(comment);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot update {}", comment, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public Comment get(int id) throws ServerException {
        LOGGER.debug("Getting comment by id {}", id);
        
        try (SqlSession session = getSession()) {
            return getCommentMapper(session).get(id);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get comment by id {}", id, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Comment> getByCreated(LocalDateTime created) throws ServerException {
        LOGGER.debug("Getting comments by created {}", created);
        
        try (SqlSession session = getSession()) {
            return getCommentMapper(session).getByCreated(created);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get comments by created {}", created, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Comment> getByAuthor(User author) throws ServerException {
        LOGGER.debug("Getting comments by {}", author);
        
        try (SqlSession session = getSession()) {
            return getCommentMapper(session).getByAuthor(author);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get comments by {}", author, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Comment> getBySection(Section section) throws ServerException {
        LOGGER.debug("Getting comments by {}", section);
        
        try (SqlSession session = getSession()) {
            return getCommentMapper(session).getBySection(section);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get comments by {}", section, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Comment> getByNoteRevision(NoteRevision revision) throws ServerException {
        LOGGER.debug("Getting comments by {}", revision);
        
        try (SqlSession session = getSession()) {
            return getCommentMapper(session).getByNoteRevision(revision);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get comments by {}", revision, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Comment> getByNote(Note note) throws ServerException {
        LOGGER.debug("Getting comments by {}", note);
        
        try (SqlSession session = getSession()) {
            return getCommentMapper(session).getByNote(note);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get comments by {}", note, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public void delete(Comment comment) throws ServerException {
        LOGGER.debug("Deleting {}", comment);
        
        try (SqlSession session = getSession()) {
            try {
                getCommentMapper(session).delete(comment);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete {}", comment, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void deleteByMostRecentNoteRevision(Note note) throws ServerException {
        LOGGER.debug("Deleting comments to most recent revision of {}", note);
        
        try (SqlSession session = getSession()) {
            try {
                getCommentMapper(session).deleteByMostRecentNoteRevision(note);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete comments to most recent revision of {}", note, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
