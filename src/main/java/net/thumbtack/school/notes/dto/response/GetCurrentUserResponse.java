package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetCurrentUserResponse {
    private int id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String login;
}
