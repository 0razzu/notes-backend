package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.Include;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class IncludeValidator implements ConstraintValidator<Include, String> {
    @Override
    public boolean isValid(String include, ConstraintValidatorContext constraintValidatorContext) {
        return include == null || include.isBlank() ||
                include.equals("onlyFollowings") || include.equals("onlyIgnore") || include.equals("notIgnore");
    }
}
