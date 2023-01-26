package ru.practicum.shareit.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BookingDateValidator.class)
public @interface BookingDateValidation {
    String message() default "Booking start time should be in the future and before booking end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
