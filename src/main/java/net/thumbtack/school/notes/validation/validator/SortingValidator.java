package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.Sorting;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class SortingValidator implements ConstraintValidator<Sorting, String> {
    @Override
    public boolean isValid(String sortingType, ConstraintValidatorContext constraintValidatorContext) {
        return sortingType == null || sortingType.isBlank() || sortingType.equals("asc") || sortingType.equals("desc");
    }
}
