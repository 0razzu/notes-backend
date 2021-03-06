package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.SectionDao;
import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.dto.request.CreateSectionRequest;
import net.thumbtack.school.notes.dto.request.RenameSectionRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import net.thumbtack.school.notes.model.UserType;
import net.thumbtack.school.notes.util.Properties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SectionService extends ServiceBase {
    protected SectionService(Properties properties, SectionDao sectionDao, SessionDao sessionDao) {
        super(properties, null, null, null, null, sectionDao, sessionDao, null);
    }
    
    
    public CreateSectionResponse create(CreateSectionRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Section section = new Section(request.getName(), user);
        
        sectionDao.insert(section);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new CreateSectionResponse(
                section.getId(),
                request.getName()
        );
    }
    
    
    public RenameSectionResponse rename(int id, RenameSectionRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Section section = sectionDao.get(id);
        
        if (!section.getCreator().equals(user))
            throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
        
        section.setName(request.getName());
        sectionDao.update(section);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new RenameSectionResponse(
                section.getId(),
                request.getName()
        );
    }
    
    
    public EmptyResponse delete(int id, String token, HttpServletResponse response) throws ServerException {
        User user = getUserByToken(token);
        Section section = sectionDao.get(id);
        
        if (section != null) {
            if (user.getType() != UserType.SUPER && !section.getCreator().equals(user))
                throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
            
            sectionDao.delete(section);
        }
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public GetSectionResponse get(int id, String token, HttpServletResponse response) throws ServerException {
        getUserByToken(token);
    
        Section section = sectionDao.get(id);
    
        if (section == null)
            throw new ServerException(ErrorCodeWithField.SECTION_NOT_FOUND);
    
        User creator = section.getCreator();
    
        updateSession(response, token, properties.getUserIdleTimeout());
        return new GetSectionResponse(
                section.getId(),
                section.getName(),
                creator.getId()
        );
    }
    
    
    public List<GetSectionsResponseItem> getAll(String token, HttpServletResponse response) throws ServerException {
        getUserByToken(token);
        
        List<Section> sections = sectionDao.getAll();
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return sections.stream().map((Section s) -> new GetSectionsResponseItem(
                s.getId(),
                s.getName()
        )).collect(Collectors.toList());
    }
}
