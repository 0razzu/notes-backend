package net.thumbtack.school.notes.controller;


import net.thumbtack.school.notes.dto.request.CreateCommentRequest;
import net.thumbtack.school.notes.dto.request.EditCommentRequest;
import net.thumbtack.school.notes.dto.response.CreateCommentResponse;
import net.thumbtack.school.notes.dto.response.EditCommentResponse;
import net.thumbtack.school.notes.error.ServerException;
import net.thumbtack.school.notes.service.CommentService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    private final CommentService commentService;
    
    
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateCommentResponse create(@Validated @RequestBody CreateCommentRequest request,
                                        @CookieValue(value = JAVA_SESSION_ID) String token,
                                        HttpServletResponse response) throws ServerException {
        return commentService.create(request, token, response);
    }
    
    
    @PutMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public EditCommentResponse edit(@PathVariable("id") int id,
                                    @Validated @RequestBody EditCommentRequest request,
                                    @CookieValue(value = JAVA_SESSION_ID) String token,
                                    HttpServletResponse response) throws ServerException {
        return commentService.edit(id, request, token, response);
    }
}
