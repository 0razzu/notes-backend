package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetNoteResponse {
    private int id;
    private String subject;
    private String body;
    private int sectionId;
    private int authorId;
    private LocalDateTime created;
    private int revisionId;
}
