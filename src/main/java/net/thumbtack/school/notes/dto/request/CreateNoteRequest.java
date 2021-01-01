package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateNoteRequest {
    @NotBlank(message = "NOT_BLANK")
    private String subject;
    @NotBlank(message = "NOT_BLANK")
    private String body;
    private int sectionId;
}
