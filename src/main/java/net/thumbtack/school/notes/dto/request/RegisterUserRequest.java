package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validation.constraint.MaxNameLength;
import net.thumbtack.school.notes.validation.constraint.MinPasswordLength;
import net.thumbtack.school.notes.validation.constraint.Name;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterUserRequest {
    @MaxNameLength
    @Name
    private String firstName;
    @MaxNameLength(nullable = true)
    @Name(nullable = true)
    private String patronymic;
    @MaxNameLength
    @Name
    private String lastName;
    @MaxNameLength
    private String login;
    @MinPasswordLength
    private String password;
}
