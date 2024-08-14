package com.nisum.userapi.exception;

import com.nisum.userapi.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    protected ResponseEntity<Object> handleServiceException(ApiException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                new ErrorResponse(exception.getMessage()),
                new HttpHeaders(),
                exception.getStatus(),
                request
        );
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                new ErrorResponse(exception.getMessage()),
                new HttpHeaders(),
                NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleOtherException(Exception exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                new ErrorResponse(exception.getMessage()),
                new HttpHeaders(),
                INTERNAL_SERVER_ERROR,
                request
        );
    }
}
