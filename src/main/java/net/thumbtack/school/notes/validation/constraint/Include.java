package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.IncludeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = IncludeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Include {
    String message() default "INCLUDE_CONSTRAINT_VIOLATION";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
