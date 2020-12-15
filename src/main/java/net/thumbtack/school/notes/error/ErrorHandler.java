package net.thumbtack.school.notes.error;


import net.thumbtack.school.notes.dto.response.error.ErrorListResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

import static net.thumbtack.school.notes.error.ErrorCodeWithField.ENDPOINT_NOT_FOUND;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.UNKNOWN_ERROR;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = new ArrayList<>();
        BindingResult result = e.getBindingResult();
        
        for (FieldError error: result.getFieldErrors()) {
            ErrorCode errorCode = ErrorCode.valueOf(error.getDefaultMessage());
            
            errors.add(new ErrorResponse(
                    errorCode.name(),
                    error.getField(),
                    errorCode.getMessage()
            ));
        }
        
        for (ObjectError error: result.getGlobalErrors()) {
            ErrorCodeWithField errorCode = ErrorCodeWithField.valueOf(error.getDefaultMessage());
            
            errors.add(new ErrorResponse(
                    errorCode.name(),
                    errorCode.getField(),
                    errorCode.getMessage()
            ));
        }
        
        return new ErrorListResponse(errors);
    }
    
    
    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleServerException(ServerException e) {
        ErrorCodeWithField errorCode = e.getErrorCode();
        
        return new ErrorListResponse(List.of(new ErrorResponse(
                errorCode.name(),
                errorCode.getField(),
                errorCode.getMessage()
        )));
    }
    
    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorListResponse handleNoHandlerException(NoHandlerFoundException e) {
        return new ErrorListResponse(List.of(new ErrorResponse(
                ENDPOINT_NOT_FOUND.name(),
                ENDPOINT_NOT_FOUND.getField(),
                ENDPOINT_NOT_FOUND.getMessage()
        )));
    }
    
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleUnknownException(Exception e) {
        return new ErrorListResponse(List.of(new ErrorResponse(
                UNKNOWN_ERROR.name(),
                UNKNOWN_ERROR.getField(),
                UNKNOWN_ERROR.getMessage()
        )));
    }
}
