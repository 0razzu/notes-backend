package net.thumbtack.school.notes.error;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    MIN_CONSTRAINT_VIOLATION("The value is too small"),
    NAME_CONSTRAINT_VIOLATION("The name is too long or empty"),
    NOT_BLANK("The field is blank"),
    PASSWORD_CONSTRAINT_VIOLATION("The password is too short"),
    SORTING_CONSTRAINT_VIOLATION("Incorrect sorting type"),
    USER_LIST_TYPE_CONSTRAINT_VIOLATION("Incorrect user list type");
    
    
    private final String message;
}
