package net.thumbtack.school.notes.error;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    INCLUDE_CONSTRAINT_VIOLATION("Incorrect note inclusion type"),
    MAX_CONSTRAINT_VIOLATION("The value is too large"),
    MIN_CONSTRAINT_VIOLATION("The value is too small"),
    NAME_CONSTRAINT_VIOLATION("The name is not valid"),
    NAME_LENGTH_CONSTRAINT_VIOLATION("The name is too long or empty"),
    NOT_BLANK("The field is blank"),
    PASSWORD_CONSTRAINT_VIOLATION("The password is too short"),
    SECTION_NAME_CONSTRAINT_VIOLATION("The section name is not valid"),
    SORTING_CONSTRAINT_VIOLATION("Incorrect sorting type"),
    TYPE_MISMATCH("Wrong field type"),
    USER_LIST_TYPE_CONSTRAINT_VIOLATION("Incorrect user list type");
    
    
    private final String message;
}
