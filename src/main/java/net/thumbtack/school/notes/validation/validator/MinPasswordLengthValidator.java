package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.database.util.Properties;
import net.thumbtack.school.notes.validation.constraint.MinPasswordLength;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
public class MinPasswordLengthValidator implements ConstraintValidator<MinPasswordLength, String> {
    private final Properties properties;
    
    
    public MinPasswordLengthValidator(Properties properties) {
        this.properties = properties;
    }
    
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password != null && password.length() >= properties.getMinPasswordLength();
    }
}
