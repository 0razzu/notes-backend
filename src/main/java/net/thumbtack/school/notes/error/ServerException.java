package net.thumbtack.school.notes.error;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class ServerException extends Exception {
    private final ErrorCodeWithField errorCode;
}
