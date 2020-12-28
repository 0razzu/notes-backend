package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.NameValidator;
import net.thumbtack.school.notes.validation.validator.SectionNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = SectionNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SectionName {
    String message() default "SECTION_NAME_CONSTRAINT_VIOLATION";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
