package net.thumbtack.school.notes.dto.request;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class RegisterUserRequest { // TODO: validation
    private String firstName;
    private String patronymic;
    private String lastName;
    private String login;
    private String password;
}
