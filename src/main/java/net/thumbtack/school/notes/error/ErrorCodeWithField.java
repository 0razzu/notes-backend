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
    NOT_PERMITTED(JAVA_SESSION_ID, "This operation is not permitted for your account type"),
    UNKNOWN_ERROR("unknown", "An unknown error has occurred"),
    USER_NOT_FOUND("id", "Cannot find a user with this id"),
    SESSION_NOT_FOUND(JAVA_SESSION_ID, "Cannot find a session with this token"),
    WRONG_OLD_PASSWORD("oldPassword", "The provided password is wrong"),
    WRONG_PASSWORD("password", "The provided password is wrong");
    
    
    private final String field;
    private final String message;
}
