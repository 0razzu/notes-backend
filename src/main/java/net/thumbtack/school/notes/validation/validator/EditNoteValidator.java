package net.thumbtack.school.notes.validation.validator;


import net.thumbtack.school.notes.dto.request.EditNoteRequest;
import net.thumbtack.school.notes.validation.constraint.EditNote;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EditNoteValidator implements ConstraintValidator<EditNote, EditNoteRequest> {
    @Override
    public boolean isValid(EditNoteRequest editNoteRequest, ConstraintValidatorContext constraintValidatorContext) {
        return editNoteRequest.getBody() != null || editNoteRequest.getSectionId() != null;
    }
}
