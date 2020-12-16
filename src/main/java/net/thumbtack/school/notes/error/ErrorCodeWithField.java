package net.thumbtack.school.notes.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

import static net.thumbtack.school.notes.database.util.Properties.JAVA_SESSION_ID;


@AllArgsConstructor
@Getter
public enum ErrorCodeWithField {
    ENDPOINT_NOT_FOUND("url", "Cannot find the requested endpoint"),
    LOGIN_EXISTS("login", "A user with this login already exists"),
    NO_COOKIE(JAVA_SESSION_ID, "There is no cookie in the request"),
    UNKNOWN_ERROR("unknown", "An unknown error has occurred"),
    SESSION_NOT_FOUND(JAVA_SESSION_ID, "Cannot find a session with this token"),
    WRONG_OLD_PASSWORD("oldPassword", "The provided password is wrong"),
    WRONG_PASSWORD("password", "The provided password is wrong");
    
    
    private final String field;
    private final String message;
}
