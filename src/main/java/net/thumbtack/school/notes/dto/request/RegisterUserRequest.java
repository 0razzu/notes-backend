package net.thumbtack.school.notes.dto.request;


import lombok.*;
import net.thumbtack.school.notes.validation.constraint.MaxNameLength;
import net.thumbtack.school.notes.validation.constraint.MinPasswordLength;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class RegisterUserRequest {
    @MaxNameLength
    private String firstName;
    @MaxNameLength(nullable = true)
    private String patronymic;
    @MaxNameLength
    private String lastName;
    @MaxNameLength
    private String login;
    @MinPasswordLength
    private String password;
}
