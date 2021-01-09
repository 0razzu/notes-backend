package net.thumbtack.school.notes.dto.response.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorListResponse {
    private List<ErrorResponse> errors;
}
