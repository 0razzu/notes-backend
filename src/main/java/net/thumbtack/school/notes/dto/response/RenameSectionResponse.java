package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RenameSectionResponse {
    private int id;
    private String name;
}
