package net.thumbtack.school.notes.error;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    NAME_CONSTRAINT_VIOLATION("The name is too long or empty"),
    NOT_BLANK("The field is blank"),
    PASSWORD_CONSTRAINT_VIOLATION("The password is too short");
    
    
    private final String message;
}
