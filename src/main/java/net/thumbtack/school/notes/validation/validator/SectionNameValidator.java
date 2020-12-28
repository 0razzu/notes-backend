package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.validation.constraint.SectionName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class SectionNameValidator implements ConstraintValidator<SectionName, String> {
    private static final Pattern PATTERN = Pattern.compile("^[А-ЯЁа-яё\\w ]+$");
    
    
    @Override
    public boolean isValid(String sectionName, ConstraintValidatorContext constraintValidatorContext) {
        return sectionName != null && !sectionName.isBlank() && PATTERN.matcher(sectionName).find();
    }
}
