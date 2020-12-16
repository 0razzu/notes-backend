package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validation.constraint.MaxNameLength;
import net.thumbtack.school.notes.validation.constraint.MinPasswordLength;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserRequest {
    @MaxNameLength
    private String firstName;
    @MaxNameLength(nullable = true)
    private String patronymic;
    @MaxNameLength
    private String lastName;
    @NotBlank(message = "NOT_BLANK")
    private String oldPassword;
    @MinPasswordLength
    private String newPassword;
}
