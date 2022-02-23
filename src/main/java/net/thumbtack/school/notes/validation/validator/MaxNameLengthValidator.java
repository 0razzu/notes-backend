package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.util.Properties;
import net.thumbtack.school.notes.validation.constraint.MaxNameLength;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
public class MaxNameLengthValidator implements ConstraintValidator<MaxNameLength, String> {
    private final Properties properties;
    private boolean nullable;
    
    
    public MaxNameLengthValidator(Properties properties) {
        this.properties = properties;
    }
    
    
    @Override
    public void initialize(MaxNameLength constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }
    
    
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return nullable && (name == null || name.isBlank()) ||
                name != null && !name.isBlank() && name.length() <= properties.getMaxNameLength();
    }
}
