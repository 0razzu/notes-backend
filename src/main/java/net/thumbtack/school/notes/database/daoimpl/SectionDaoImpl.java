package net.thumbtack.school.notes.database.daoimpl;


import net.thumbtack.school.notes.database.dao.SectionDao;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


@Repository("sectionDao")
public class SectionDaoImpl extends DaoImplBase implements SectionDao {
    private final Logger LOGGER = LoggerFactory.getLogger(SectionDaoImpl.class);
    
    
    @Override
    public void insert(Section section) throws ServerException {
        LOGGER.debug("Inserting {}", section);
        
        try (SqlSession session = getSession()) {
            try {
                getSectionMapper(session).insert(section);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot insert {}", section, e);
                session.rollback();
                if (e.getCause().getClass() == SQLIntegrityConstraintViolationException.class)
                    throw new ServerException(ErrorCodeWithField.SECTION_EXISTS);
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public void update(Section section) throws ServerException {
        LOGGER.debug("Updating {}", section);
        
        try (SqlSession session = getSession()) {
            try {
                getSectionMapper(session).update(section);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot update {}", section, e);
                session.rollback();
                if (e.getCause().getClass() == SQLIntegrityConstraintViolationException.class)
                    throw new ServerException(ErrorCodeWithField.SECTION_EXISTS);
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
    
    
    @Override
    public Section get(int id) throws ServerException {
        LOGGER.debug("Getting section by id {}", id);
        
        try (SqlSession session = getSession()) {
            return getSectionMapper(session).get(id);
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get section by id {}", id, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Section> getByCreator(User creator) throws ServerException {
        LOGGER.debug("Getting sections by creator {}", creator);
        
        try (SqlSession session = getSession()) {
            List<Section> sections = getSectionMapper(session).getByCreator(creator);
            
            for (Section section: sections)
                section.setCreator(creator);
            
            return sections;
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get sections by creator {}", creator, e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public List<Section> getAll() throws ServerException {
        LOGGER.debug("Getting all sections");
        
        try (SqlSession session = getSession()) {
            return getSectionMapper(session).getAll();
        } catch (RuntimeException e) {
            LOGGER.info("Cannot get all sections", e);
            throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
        }
    }
    
    
    @Override
    public void delete(Section section) throws ServerException {
        LOGGER.debug("Deleting {}", section);
        
        try (SqlSession session = getSession()) {
            try {
                getSectionMapper(session).delete(section);
            } catch (RuntimeException e) {
                LOGGER.info("Cannot delete {}", section, e);
                session.rollback();
                throw new ServerException(ErrorCodeWithField.DATABASE_ERROR);
            }
            
            session.commit();
        }
    }
}
