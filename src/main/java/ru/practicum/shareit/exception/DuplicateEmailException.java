package ru.practicum.shareit.exception;

public class DublicateEmailException extends RuntimeException{
    public DublicateEmailException() {
        super();
    }

    public DublicateEmailException(String message) {
        super(message);
    }
}
