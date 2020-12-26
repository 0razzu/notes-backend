package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.MaxNameLengthValidator;
import net.thumbtack.school.notes.validation.validator.MinValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = MinValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Min {
    String message() default "MIN_CONSTRAINT_VIOLATION";
    int value();
    boolean nullable() default true;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
