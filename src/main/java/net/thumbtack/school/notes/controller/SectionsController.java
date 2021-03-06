package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.CreateSectionRequest;
import net.thumbtack.school.notes.dto.request.RenameSectionRequest;
import net.thumbtack.school.notes.dto.response.*;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.SectionService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static net.thumbtack.school.notes.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api/sections")
public class SectionsController {
    private final SectionService sectionService;
    
    
    public SectionsController(SectionService sectionService) {
        this.sectionService = sectionService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateSectionResponse create(@Validated @RequestBody CreateSectionRequest request,
                                        @CookieValue(value = JAVA_SESSION_ID) String token,
                                        HttpServletResponse response) throws ServerException {
        return sectionService.create(request, token, response);
    }
    
    
    @PutMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RenameSectionResponse rename(@PathVariable("id") int id,
                                        @Validated @RequestBody RenameSectionRequest request,
                                        @CookieValue(value = JAVA_SESSION_ID) String token,
                                        HttpServletResponse response) throws ServerException {
        return sectionService.rename(id, request, token, response);
    }
    
    
    @DeleteMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse delete(@PathVariable("id") int id,
                                @CookieValue(value = JAVA_SESSION_ID) String token,
                                HttpServletResponse response) throws ServerException {
        return sectionService.delete(id, token, response);
    }
    
    
    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetSectionResponse get(@PathVariable("id") int id,
                                  @CookieValue(value = JAVA_SESSION_ID) String token,
                                  HttpServletResponse response) throws ServerException {
        return sectionService.get(id, token, response);
    }
    
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetSectionsResponseItem> getAll(@CookieValue(value = JAVA_SESSION_ID) String token,
                                                HttpServletResponse response) throws ServerException {
        return sectionService.getAll(token, response);
    }
}
