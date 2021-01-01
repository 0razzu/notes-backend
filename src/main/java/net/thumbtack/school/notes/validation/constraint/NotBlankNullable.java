package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.NotBlankNullableValidator;
import net.thumbtack.school.notes.validation.validator.SectionNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = NotBlankNullableValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankNullable {
    String message() default "NOT_BLANK";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
