package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.SortingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = SortingValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sorting {
    String message() default "SORTING_CONSTRAINT_VIOLATION";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
