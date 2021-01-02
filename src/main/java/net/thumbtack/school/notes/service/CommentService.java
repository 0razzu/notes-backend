package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.dto.request.CreateCommentRequest;
import net.thumbtack.school.notes.dto.response.CreateCommentResponse;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.Comment;
import net.thumbtack.school.notes.model.Note;
import net.thumbtack.school.notes.model.NoteRevision;
import net.thumbtack.school.notes.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;


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
        
        LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC")).withNano(0);
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
}
