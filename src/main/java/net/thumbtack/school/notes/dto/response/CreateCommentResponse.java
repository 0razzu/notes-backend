package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCommentResponse {
    private int id;
    private String body;
    private int noteId;
    private int authorId;
    private int revisionId;
    private LocalDateTime created;
}
