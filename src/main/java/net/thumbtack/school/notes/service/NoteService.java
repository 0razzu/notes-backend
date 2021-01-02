package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.view.NoteView;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NoteService extends ServiceBase {
    protected NoteService(Properties properties, CommentDao commentDao, NoteDao noteDao, SectionDao sectionDao,
                          SessionDao sessionDao, UserDao userDao) {
        super(properties, commentDao, noteDao, null, sectionDao, sessionDao, userDao);
    }
    
    
    public CreateNoteResponse create(CreateNoteRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User author = getUserByToken(token);
        Section section = sectionDao.get(request.getSectionId());
        
        if (section == null)
            throw new ServerException(ErrorCodeWithField.SECTION_NOT_FOUND);
        
        LocalDateTime created = getCurrentTime();
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
    
    
    public GetNoteResponse get(int id, String token, HttpServletResponse response) throws ServerException {
        getUserByToken(token);
        
        NoteView note = noteDao.getView(id);
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new GetNoteResponse(
                note.getId(),
                note.getSubject(),
                note.getBody(),
                note.getSectionId(),
                note.getAuthorId(),
                note.getCreated(),
                note.getRevisionId()
        );
    }
    
    
    public EditNoteResponse edit(int id, EditNoteRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND);
        
        if (!note.getAuthor().equals(user))
            throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
        
        String body = request.getBody();
        NoteRevision revision = null;
        if (body != null)
            revision = new NoteRevision(body, LocalDateTime.now(ZoneId.of("UTC")), note);
        
        Integer sectionId = request.getSectionId();
        if (sectionId != null) {
            Section section = sectionDao.get(sectionId);
            
            if (section == null)
                throw new ServerException(ErrorCodeWithField.SECTION_NOT_FOUND);
            
            note.setSection(section);
        }
        else
            note = null;
        
        noteDao.update(note, revision);
        
        NoteView view = noteDao.getView(id);
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EditNoteResponse(
                id,
                view.getSubject(),
                view.getBody(),
                view.getSectionId(),
                view.getAuthorId(),
                view.getCreated(),
                view.getRevisionId()
        );
    }
    
    
    public EmptyResponse delete(int id, String token, HttpServletResponse response) throws ServerException {
        User user = getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note != null) {
            if (!note.getAuthor().equals(user))
                throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
            
            noteDao.delete(note);
        }
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public List<GetNoteCommentsResponseItem> getComments(int id, String token, HttpServletResponse response)
            throws ServerException {
        getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND);
        
        List<Comment> comments = commentDao.getByNote(note);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return comments.stream().map((Comment c) -> new GetNoteCommentsResponseItem(
                c.getId(),
                c.getBody(),
                id,
                c.getAuthor().getId(),
                c.getNoteRevision().getId(),
                c.getCreated()
        )).collect(Collectors.toList());
    }
}
