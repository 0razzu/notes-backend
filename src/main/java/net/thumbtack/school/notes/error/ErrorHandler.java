package net.thumbtack.school.notes.error;


import net.thumbtack.school.notes.dto.response.error.ErrorListResponse;
import net.thumbtack.school.notes.dto.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static net.thumbtack.school.notes.error.ErrorCode.TYPE_MISMATCH;
import static net.thumbtack.school.notes.error.ErrorCodeWithField.*;


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
    
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleConstraintException(ConstraintViolationException e) {
        List<ErrorResponse> errors = new ArrayList<>();
        
        for (ConstraintViolation<?> error: e.getConstraintViolations()) {
            ErrorCode errorCode = ErrorCode.valueOf(error.getMessage());
            
            errors.add(new ErrorResponse(
                    errorCode.name(),
                    error.getPropertyPath().toString().split("\\.")[1],
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
    
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return new ErrorListResponse(List.of(new ErrorResponse(
                METHOD_NOT_SUPPORTED.name(),
                METHOD_NOT_SUPPORTED.getField(),
                METHOD_NOT_SUPPORTED.getMessage()
        )));
    }
    
    
    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleNoCookieException(MissingRequestCookieException e) {
        return new ErrorListResponse(List.of(new ErrorResponse(
                NO_COOKIE.name(),
                NO_COOKIE.getField(),
                NO_COOKIE.getMessage()
        )));
    }
    
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorListResponse handleArgTypeException(MethodArgumentTypeMismatchException e) {
        return new ErrorListResponse(List.of(new ErrorResponse(
                TYPE_MISMATCH.name(),
                e.getParameter().getParameterName(),
                TYPE_MISMATCH.getMessage()
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
