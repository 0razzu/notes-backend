package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.CreateCommentRequest;
import net.thumbtack.school.notes.dto.request.EditCommentRequest;
import net.thumbtack.school.notes.dto.response.CreateCommentResponse;
import net.thumbtack.school.notes.dto.response.EditCommentResponse;
import net.thumbtack.school.notes.dto.response.EmptyResponse;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;


@Service
public class CommentService extends ServiceBase {
    protected CommentService(Properties properties, CommentDao commentDao,
                             NoteDao noteDao, NoteRevisionDao noteRevisionDao, SectionDao sectionDao,
                             SessionDao sessionDao, UserDao userDao) {
        super(properties, commentDao, noteDao, noteRevisionDao, sectionDao, sessionDao, userDao);
    }
    
    
    public CreateCommentResponse create(CreateCommentRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User author = getUserByToken(token);
        Note note = noteDao.get(request.getNoteId());
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND_NOTE_ID);
        
        LocalDateTime created = getCurrentTime();
        String body = request.getBody();
        NoteRevision revision = noteRevisionDao.getMostRecent(note);
        Comment comment = new Comment(created, body, author, revision);
        
        commentDao.insert(comment);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new CreateCommentResponse(
                comment.getId(),
                body,
                note.getId(),
                author.getId(),
                revision.getId(),
                created
        );
    }
    
    
    public EditCommentResponse edit(int id, EditCommentRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Comment comment = commentDao.get(id);
        
        if (comment == null)
            throw new ServerException(ErrorCodeWithField.COMMENT_NOT_FOUND);
        
        if (!comment.getAuthor().equals(user))
            throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
        
        LocalDateTime created = getCurrentTime();
        String body = request.getBody();
        Note note = comment.getNoteRevision().getNote();
        NoteRevision revision = noteRevisionDao.getMostRecent(note);
        
        comment.setCreated(created);
        comment.setBody(body);
        comment.setNoteRevision(revision);
        
        commentDao.update(comment);
    
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EditCommentResponse(
                id,
                body,
                note.getId(),
                user.getId(),
                revision.getId(),
                created
        );
    }
    
    
    public EmptyResponse delete(int id, String token, HttpServletResponse response) throws ServerException {
        User user = getUserByToken(token);
        Comment comment = commentDao.get(id);
        
        if (comment != null) {
            if (user.getType() != UserType.SUPER && !comment.getAuthor().equals(user))
                throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
            
            commentDao.delete(comment);
        }
    
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
}
