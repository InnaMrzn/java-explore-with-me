package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class EwmExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        log.error(errorMessage.toString());
        return new ApiError(List.of(ex.getMessage()), ex.getMessage(), ex.getCause().getMessage(),
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingRequiredHeaderException(MissingRequestHeaderException ex) {
        log.error(ex.getMessage());
        return new ApiError(List.of(ex.getMessage()), ex.getMessage(), ex.getMessage(),
                HttpStatus.BAD_REQUEST.name(), LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleWrongActionException(WrongActionException ex) {
        log.error(ex.getMessage());
        return new ApiError(List.of(ex.getMessage()), ex.getMessage(), ex.getMessage(),
                HttpStatus.FORBIDDEN.name(), LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException ex) {
        log.error(ex.getMessage());
        return new ApiError(List.of(ex.getMessage()), ex.getMessage(), ex.getMessage(),
                HttpStatus.NOT_FOUND.name(), LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable ex) {
        ex.printStackTrace();
        log.error(ex.getClass() + " " + ex.getMessage());
        return new ApiError(List.of(ex.getMessage()), ex.getMessage(), ex.getCause().getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

}

