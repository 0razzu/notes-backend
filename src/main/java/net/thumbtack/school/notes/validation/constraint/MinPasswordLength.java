package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.MinPasswordLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = MinPasswordLengthValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinPasswordLength {
    String message() default "Password is too short";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
