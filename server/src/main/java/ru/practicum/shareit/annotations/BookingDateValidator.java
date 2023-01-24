package ru.practicum.shareit.annotations;

import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateValidator implements ConstraintValidator<BookingDateValidation, BookingDto> {

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        return bookingDto.getStart().isAfter(LocalDateTime.now()) && bookingDto.getStart().isBefore(bookingDto.getEnd());
    }
}
