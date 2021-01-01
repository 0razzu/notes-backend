package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.NotBlankNullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotBlankNullableValidator implements ConstraintValidator<NotBlankNullable, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || !value.isBlank();
    }
}
