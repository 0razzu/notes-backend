package net.thumbtack.school.notes.error;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCodeWithField {
    ENDPOINT_NOT_FOUND("url", "Cannot find the requested endpoint"),
    LOGIN_EXISTS("login", "A user with this login already exists"),
    UNKNOWN_ERROR("unknown", "An unknown error has occurred"),
    SESSION_NOT_FOUND("JAVASESSIONID", "Cannot find a session with this token"),
    WRONG_PASSWORD("password", "The provided password is wrong");
    
    
    private final String field;
    private final String message;
}
