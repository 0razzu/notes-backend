package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.dto.request.RateNoteRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.NoteService;
import net.thumbtack.school.notes.validation.constraint.Include;
import net.thumbtack.school.notes.validation.constraint.Min;
import net.thumbtack.school.notes.validation.constraint.Sorting;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

import static net.thumbtack.school.notes.util.Properties.JAVA_SESSION_ID;


@RestController
@Validated
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
    
    
    @GetMapping(path = "/{id}/revisions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetNoteRevisionsItem> getRevisions(@PathVariable("id") int id,
                                                   @RequestParam(defaultValue = "false") boolean comments,
                                                   @CookieValue(value = JAVA_SESSION_ID) String token,
                                                   HttpServletResponse response) throws ServerException {
        return noteService.getRevisions(id, comments, token, response);
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
    public EmptyResponse rate(@PathVariable("id") int id,
                              @Validated @RequestBody RateNoteRequest request,
                              @CookieValue(value = JAVA_SESSION_ID) String token,
                              HttpServletResponse response) throws ServerException {
        return noteService.rate(id, request, token, response);
    }
    
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetNotesResponseItem> getNotes(@RequestParam(required = false) Integer sectionId,
                                               @RequestParam(required = false) @Sorting String sortByRating,
                                               @RequestParam(required = false) List<String> tags,
                                               @RequestParam(defaultValue = "false") boolean allTags,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeFrom,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeTo,
                                               @RequestParam(required = false) Integer user,
                                               @RequestParam(required = false) @Include String include,
                                               @RequestParam(defaultValue = "false") boolean comments,
                                               @RequestParam(defaultValue = "false") boolean allVersions,
                                               @RequestParam(defaultValue = "false") boolean commentVersion,
                                               @RequestParam(required = false) @Min(0) Integer from,
                                               @RequestParam(required = false) @Min(1) Integer count,
                                               @CookieValue(value = JAVA_SESSION_ID) String token,
                                               HttpServletResponse response) throws ServerException {
        return noteService.getNotes(sectionId, sortByRating, tags, allTags, timeFrom, timeTo, user, include,
                comments, allVersions, commentVersion, from, count, token, response);
    }
}
