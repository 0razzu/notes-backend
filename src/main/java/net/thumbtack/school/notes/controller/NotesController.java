package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.RateNoteRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.NoteService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NoteService noteService;
    
    
    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateNoteResponse create(@Validated @RequestBody CreateNoteRequest request,
                                     @CookieValue(value = JAVA_SESSION_ID) String token,
                                     HttpServletResponse response) throws ServerException {
        return noteService.create(request, token, response);
    }
    
    
    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetNoteResponse get(@PathVariable("id") int id,
                               @CookieValue(value = JAVA_SESSION_ID) String token,
                               HttpServletResponse response) throws ServerException {
        return noteService.get(id, token, response);
    }
    
    
    @PutMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EditNoteResponse edit(@PathVariable("id") int id,
                                 @Validated @RequestBody EditNoteRequest request,
                                 @CookieValue(value = JAVA_SESSION_ID) String token,
                                 HttpServletResponse response) throws ServerException {
        return noteService.edit(id, request, token, response);
    }
    
    
    @DeleteMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse delete(@PathVariable("id") int id,
                                @CookieValue(value = JAVA_SESSION_ID) String token,
                                HttpServletResponse response) throws ServerException {
        return noteService.delete(id, token, response);
    }
    
    
    @GetMapping(path = "/{id}/comments",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetNoteCommentsResponseItem> getComments(@PathVariable("id") int id,
                                                         @CookieValue(value = JAVA_SESSION_ID) String token,
                                                         HttpServletResponse response) throws ServerException {
        return noteService.getComments(id, token, response);
    }
    
    
    @DeleteMapping(path = "/{id}/comments",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse deleteCommentsToMostRecentRevision(@PathVariable("id") int id,
                                                            @CookieValue(value = JAVA_SESSION_ID) String token,
                                                            HttpServletResponse response) throws ServerException {
        return noteService.deleteCommentsToMostRecentRevision(id, token, response);
    }
    
    
    @PostMapping(path = "/{id}/rating",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse create(@PathVariable("id") int id,
                                @Validated @RequestBody RateNoteRequest request,
                                @CookieValue(value = JAVA_SESSION_ID) String token,
                                HttpServletResponse response) throws ServerException {
        return noteService.rate(id, request, token, response);
    }
}