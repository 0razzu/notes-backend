package net.thumbtack.school.notes.validation.constraint;


import net.thumbtack.school.notes.validation.validator.UserListTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UserListTypeValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserListType {
    String message() default "USER_LIST_TYPE_CONSTRAINT_VIOLATION";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
