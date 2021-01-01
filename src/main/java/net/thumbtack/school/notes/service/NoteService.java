package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.NoteDao;
import net.thumbtack.school.notes.database.dao.SectionDao;
import net.thumbtack.school.notes.database.dao.SessionDao;
import net.thumbtack.school.notes.database.dao.UserDao;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.response.CreateNoteResponse;
import net.thumbtack.school.notes.dto.response.CreateSectionResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.Section;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
public class NoteService extends ServiceBase {
    protected NoteService(Properties properties, NoteDao noteDao, SectionDao sectionDao,
                          SessionDao sessionDao, UserDao userDao) {
        super(properties, noteDao, sectionDao, sessionDao, userDao);
    }
    
    
    public CreateNoteResponse create(CreateNoteRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User author = getUserByToken(token);
        Section section = sectionDao.get(request.getSectionId());
        LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC")).withNano(0);
        Note note = new Note(request.getSubject(), author, created, section);
        NoteRevision revision = new NoteRevision(request.getBody(), created, note);
        
        noteDao.insert(note, revision);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new CreateNoteResponse(
                note.getId(),
                note.getSubject(),
                revision.getBody(),
                section.getId(),
                author.getId(),
                created,
                revision.getId()
        );
    }
}
