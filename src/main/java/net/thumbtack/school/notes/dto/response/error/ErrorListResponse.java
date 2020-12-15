package net.thumbtack.school.notes.dto.response.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Setter
@Getter
public class ErrorListResponse {
    private List<ErrorResponse> errors;
}
