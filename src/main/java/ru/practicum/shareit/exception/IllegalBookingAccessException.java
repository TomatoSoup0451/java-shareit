package ru.practicum.shareit.exception;

public class IllegalBookingAccessException extends RuntimeException {

    public IllegalBookingAccessException(String message) {
        super(message);
    }
}
