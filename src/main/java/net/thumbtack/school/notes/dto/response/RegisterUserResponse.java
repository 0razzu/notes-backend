package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterUserResponse {
    private String firstName;
    private String patronymic;
    private String lastName;
    private String login;
}
