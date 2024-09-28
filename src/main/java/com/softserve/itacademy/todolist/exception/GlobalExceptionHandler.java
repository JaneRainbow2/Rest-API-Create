package com.softserve.itacademy.todolist.exception;

import com.softserve.itacademy.todolist.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Handler 'handleMethodArgumentNotValid' caught 'MethodArgumentNotValidException'");
        ExceptionDto exception = new ExceptionDto(
                LocalDateTime.now(), ex.getMessage(), 409
        );

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<ExceptionDto> handleNullEntityReferenceException(NullEntityReferenceException ex, WebRequest request) {
        log.error("Handler 'handleNullEntityReferenceException' caught 'NullEntityReferenceException'");
        ExceptionDto exception = new ExceptionDto(
                LocalDateTime.now(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("Handler 'handleEntityNotFoundException' caught 'EntityNotFoundException'");
        ExceptionDto exception = new ExceptionDto(
                LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> accessDeniedErrorHandler(AccessDeniedException ex, WebRequest request) {
        log.error("Handler 'accessDeniedErrorHandler' caught 'AccessDeniedException'");
        ExceptionDto exceptionDto = new ExceptionDto(
                LocalDateTime.now(),
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value()
        );
        return new ResponseEntity<>(exceptionDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> internalServerErrorHandler(Exception ex, WebRequest request) {
        log.error("Handler 'internalServerErrorHandler' caught 'Exception'");
        ExceptionDto exception = new ExceptionDto(
                LocalDateTime.now(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}