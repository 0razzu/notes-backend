package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String message() default "NAME_CONSTRAINT_VIOLATION";
    boolean nullable() default false;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
