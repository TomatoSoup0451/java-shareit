package ru.practicum.shareit.exception;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException() {
        super();
    }

    public UnknownStateException(String message) {
        super(message);
    }
}
