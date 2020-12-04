package net.thumbtack.school.notes.dto.response;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class RegisterUserResponse {
    private String firstName;
    private String patronymic;
    private String lastName;
    private String login;
}
