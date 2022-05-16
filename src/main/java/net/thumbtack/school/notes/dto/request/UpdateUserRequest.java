package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validation.constraint.MaxNameLength;
import net.thumbtack.school.notes.validation.constraint.MinPasswordLength;
import net.thumbtack.school.notes.validation.constraint.Name;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserRequest {
    @MaxNameLength
    @Name(nullable = true)
    private String firstName;
    @MaxNameLength(nullable = true)
    @Name(nullable = true)
    private String patronymic;
    @MaxNameLength
    @Name(nullable = true)
    private String lastName;
    @NotBlank(message = "NOT_BLANK")
    private String oldPassword;
    @MinPasswordLength
    private String newPassword;
}
