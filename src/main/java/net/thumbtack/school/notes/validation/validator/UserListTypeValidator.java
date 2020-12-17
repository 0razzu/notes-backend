package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.UserListType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UserListTypeValidator implements ConstraintValidator<UserListType, String> {
    @Override
    public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
        return type == null || type.isBlank() ||
                type.equals("highRating") || type.equals("lowRating") ||
                type.equals("following") || type.equals("followers") || type.equals("ignore") || type.equals("ignoredBy") ||
                type.equals("deleted") || type.equals("super");
    }
}
