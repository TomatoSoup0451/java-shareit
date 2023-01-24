package ru.practicum.shareit.exception;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public void handleValidationException(Exception e) {
        log.warn(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IdNotFoundException.class)
    public void handleIdNotFound(Exception e) {
        log.warn(e.getMessage());
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public void handleForbiddenException(Exception e) {
        log.warn(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn(NestedExceptionUtils.getMostSpecificCause(e).getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UnsupportedOperationException.class)
    public void handleUnsupportedOperationException(UnsupportedOperationException e) {
        log.warn(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalBookingAccessException.class)
    public void handleIllegalBookingAccessException(IllegalBookingAccessException e) {
        log.warn(e.getMessage());
    }

    @ExceptionHandler(UnknownStateException.class)
    public ResponseEntity<ErrorResponse> handleUnknownStateException(UnknownStateException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @Value
    private class ErrorResponse {
        String error;
    }
}
