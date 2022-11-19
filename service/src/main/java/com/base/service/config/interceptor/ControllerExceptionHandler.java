package com.base.service.config.interceptor;

import com.base.commons.enums.ResponseType;
import com.base.commons.exception.BadRequestException;
import com.base.commons.exception.UnauthorizedException;
import com.base.commons.response.GenericResponse;
import com.base.commons.response.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ControllerExceptionHandler is the Exception handler which takes care of the Exceptions as controller advice. When any
 * exception is thrown then this advice will catch and will return the proper response to the client with relevant
 * information.
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    /**
     * handleValidationExceptions takes care of all the validation exception that are being thrown as part of Json
     * validator using @Valid annotation
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder("Validation Error: ");
        ex.getBindingResult()
                .getAllErrors()
                .forEach(
                        (error) -> {
                            String fieldName = ((FieldError) error).getField();
                            String errorMessage = error.getDefaultMessage();
                            message.append(String.format("%s: %s", fieldName, errorMessage));
                        });
        log.error(message.toString().trim(), ex);
        GenericResponse response = new GenericResponse(new Status(ResponseType.ERROR, message.toString().trim()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * handleUnauthorizedExceptions takes care of all the UnauthorizedExceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedExceptions(UnauthorizedException ex) {
        log.error(ex.getMessage(), ex);
        GenericResponse response = new GenericResponse(new Status(ResponseType.ERROR, ex.getMessage()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * handleBadRequestExceptions takes care of all the UnauthorizedExceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestExceptions(BadRequestException ex) {
        log.error(ex.getMessage(), ex);
        GenericResponse response = new GenericResponse(new Status(ResponseType.ERROR, ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * handleRunTimeExceptions takes care of all the RunTimeExceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRunTimeExceptions(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        GenericResponse response = new GenericResponse(new Status(ResponseType.ERROR, ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * handleException takes care of all the Exceptions
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        GenericResponse response = new GenericResponse(new Status(ResponseType.ERROR, ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
