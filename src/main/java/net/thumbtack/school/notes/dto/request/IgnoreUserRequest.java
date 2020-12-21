package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class IgnoreUserRequest {
    @NotBlank(message = "NOT_BLANK")
    private String login;
}
