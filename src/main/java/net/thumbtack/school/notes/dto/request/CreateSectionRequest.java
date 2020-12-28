package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validation.constraint.SectionName;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSectionRequest {
    @SectionName
    private String name;
}
