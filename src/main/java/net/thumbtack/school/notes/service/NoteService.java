package net.thumbtack.school.notes.service;


import net.thumbtack.school.notes.database.dao.*;
import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.RateNoteRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ErrorCodeWithField;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.model.*;
import net.thumbtack.school.notes.util.Properties;
import net.thumbtack.school.notes.view.CommentView;
import net.thumbtack.school.notes.view.NoteRevisionView;
import net.thumbtack.school.notes.view.NoteView;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NoteService extends ServiceBase {
    protected NoteService(Properties properties, CommentDao commentDao,
                          NoteDao noteDao, NoteRevisionDao noteRevisionDao, RatingDao ratingDao, SectionDao sectionDao,
                          SessionDao sessionDao) {
        super(properties, commentDao, noteDao, noteRevisionDao, ratingDao, sectionDao, sessionDao, null);
    }
    
    
    public CreateNoteResponse create(CreateNoteRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User author = getUserByToken(token);
        Section section = sectionDao.get(request.getSectionId());
        
        if (section == null)
            throw new ServerException(ErrorCodeWithField.SECTION_NOT_FOUND_SECTION_ID);
        
        LocalDateTime created = getCurrentTime();
        Note note = new Note(author, created, section);
        NoteRevision revision = new NoteRevision(request.getSubject(), request.getBody(), created, note);
        
        noteDao.insert(note, revision);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new CreateNoteResponse(
                note.getId(),
                revision.getSubject(),
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
        
        NoteView view = noteDao.getView(id);
        
        String body = request.getBody();
        NoteRevision revision = null;
        if (body != null)
            revision = new NoteRevision(view.getSubject(), body, LocalDateTime.now(ZoneId.of("UTC")), note);
        
        Integer sectionId = request.getSectionId();
        if (sectionId != null) {
            Section section = sectionDao.get(sectionId);
            
            if (section == null)
                throw new ServerException(ErrorCodeWithField.SECTION_NOT_FOUND_SECTION_ID);
            
            note.setSection(section);
        }
        else
            note = null;
        
        noteDao.update(note, revision);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EditNoteResponse(
                id,
                view.getSubject(),
                body == null? view.getBody() : body,
                sectionId == null? view.getSectionId() : sectionId,
                view.getAuthorId(),
                view.getCreated(),
                revision == null? view.getRevisionId() : revision.getId()
        );
    }
    
    
    public EmptyResponse delete(int id, String token, HttpServletResponse response) throws ServerException {
        User user = getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note != null) {
            if (user.getType() != UserType.SUPER && !note.getAuthor().equals(user))
                throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
            
            noteDao.delete(note);
        }
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public List<GetNoteRevisionsItem> getRevisions(int id, boolean comments, String token, HttpServletResponse response)
            throws ServerException {
        getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND);
        
        List<NoteRevision> revisions = noteRevisionDao.getByNote(note);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return revisions.stream().map(r -> new GetNoteRevisionsItem(
                r.getId(),
                r.getBody(),
                r.getCreated(),
                comments?
                        r.getComments().stream().map(c -> new GetNoteRevisionsItemComment(
                                c.getId(),
                                c.getBody(),
                                c.getAuthor().getId(),
                                c.getCreated()
                        )).collect(Collectors.toList()) :
                        null
        )).collect(Collectors.toList());
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
    
    
    public EmptyResponse deleteCommentsToMostRecentRevision(int id, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note != null) {
            if (user.getType() != UserType.SUPER && !note.getAuthor().equals(user))
                throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
            
            commentDao.deleteByMostRecentNoteRevision(note);
        }
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public EmptyResponse rate(int id, RateNoteRequest request, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND);
    
        if (user.equals(note.getAuthor()))
            throw new ServerException(ErrorCodeWithField.NOT_PERMITTED);
    
        ratingDao.insert(new Rating(request.getRating(), user), note);
    
        updateSession(response, token, properties.getUserIdleTimeout());
        return new EmptyResponse();
    }
    
    
    public GetCurrentUserMarkResponse getCurrentUserMark(int id, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        Note note = noteDao.get(id);
        
        if (note == null)
            throw new ServerException(ErrorCodeWithField.NOTE_NOT_FOUND);
        
        Rating rating = ratingDao.get(note, user);
        
        if (rating == null)
            return new GetCurrentUserMarkResponse(null);
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return new GetCurrentUserMarkResponse(rating.getValue());
    }
    
    
    public List<GetNotesResponseItem> getNotes(Integer sectionId, String sortByRating, List<String> tags, boolean allTags,
                                               LocalDateTime timeFrom, LocalDateTime timeTo,
                                               Integer author, String include,
                                               boolean comments, boolean allVersions, boolean commentVersion,
                                               Integer from, Integer count, String token, HttpServletResponse response)
            throws ServerException {
        User user = getUserByToken(token);
        
        if (allTags)
            for (int i = 0; i < tags.size(); i++)
                tags.add(i, "+" + tags.remove(i));
        
        List<NoteView> notes = noteDao.getAllByParams(
                sectionId, sortByRating,
                tags == null? null : String.join(" ", tags),
                timeFrom, timeTo,
                author, user.getId(), include,
                comments, allVersions, commentVersion,
                from, count
        );
        List<GetNotesResponseItem> responseList = new ArrayList<>(notes.size());
        
        for (NoteView noteView: notes) {
            responseList.add(new GetNotesResponseItem(
                    noteView.getId(),
                    noteView.getSubject(),
                    noteView.getBody(),
                    noteView.getSectionId(),
                    noteView.getAuthorId(),
                    noteView.getCreated(),
                    allVersions?
                            noteView.getRevisions().stream().map((NoteRevisionView r) ->
                                    new GetNotesResponseItemRevision(
                                            r.getId(),
                                            r.getBody(),
                                            r.getCreated(),
                                            comments?
                                                    r.getComments().stream().map((CommentView c) ->
                                                            new GetNotesResponseItemComment(
                                                                    c.getId(),
                                                                    c.getBody(),
                                                                    c.getAuthorId(),
                                                                    c.getNoteRevisionId(),
                                                                    c.getCreated()
                                                            )).collect(Collectors.toList()) :
                                                    null
                                    )).collect(Collectors.toList()) :
                            null,
                    comments?
                            noteView.getComments().stream().map((CommentView c) ->
                                    new GetNotesResponseItemComment(
                                            c.getId(),
                                            c.getBody(),
                                            c.getAuthorId(),
                                            c.getNoteRevisionId(),
                                            c.getCreated()
                                    )).collect(Collectors.toList()) :
                            null
            ));
        }
        
        updateSession(response, token, properties.getUserIdleTimeout());
        return responseList;
    }
}
