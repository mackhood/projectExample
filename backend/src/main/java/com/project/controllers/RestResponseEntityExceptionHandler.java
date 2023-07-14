package com.project.controllers;

import com.project.Dtos.ApiError;
import com.project.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build(),
                headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build(),
                headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, ApiError.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build(),
                headers, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class, ProjectNotFoundException.class})
    public ResponseEntity<ApiError> handleConflict(Exception ex, WebRequest request) {

        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiError> handleAlreadyReservedException(Exception ex, WebRequest request) {

        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UserAlreadyExistsInProjectException.class})
    public ResponseEntity<ApiError> handleConflictBadRequest(Exception ex, WebRequest request) {

        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ApiError> handleConflict(UnAuthorizedException exception) {

        ApiError error = ApiError.builder()
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
