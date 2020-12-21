package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class NameValidator implements ConstraintValidator<Name, String> {
    private static final Pattern PATTERN = Pattern.compile("^[A-ZА-ЯЁ]([ -]?[A-Za-zА-ЯЁа-яё])*$");
    private boolean nullable;
    
    
    @Override
    public void initialize(Name constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }
    
    
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return nullable && (name == null || name.isBlank()) ||
                name != null && PATTERN.matcher(name).find();
    }
}
