package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validation.constraint.NotBlankNullable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditNoteRequest {
    @NotBlankNullable
    private String body;
    private Integer sectionId;
}
