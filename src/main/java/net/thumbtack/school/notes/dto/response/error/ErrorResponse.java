package net.thumbtack.school.notes.dto.response.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.notes.error.ErrorCode;


@AllArgsConstructor
@Data
public class ErrorResponse {
    private String errorCode;
    private String field;
    private String message;
}
