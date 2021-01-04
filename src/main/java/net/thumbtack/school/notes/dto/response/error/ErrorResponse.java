package net.thumbtack.school.notes.dto.response.error;


import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class ErrorResponse {
    private String errorCode;
    private String field;
    private String message;
}
