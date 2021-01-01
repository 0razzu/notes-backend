package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.CreateNoteRequest;
import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.dto.response.CreateNoteResponse;
import net.thumbtack.school.notes.dto.response.EditNoteResponse;
import net.thumbtack.school.notes.dto.response.GetNoteResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.NoteService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
}
