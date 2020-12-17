package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.Min;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MinValidator implements ConstraintValidator<Min, Integer> {
    private int minValue;
    private boolean nullable;
    
    
    @Override
    public void initialize(Min constraintAnnotation) {
        minValue = constraintAnnotation.value();
        nullable = constraintAnnotation.nullable();
    }
    
    
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return nullable && value == null ||
                value != null && value >= minValue;
    }
}
