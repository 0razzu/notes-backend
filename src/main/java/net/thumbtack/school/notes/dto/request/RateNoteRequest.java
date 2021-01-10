package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class RateNoteRequest {
    @Min(value = 1, message = "MIN_CONSTRAINT_VIOLATION")
    @Max(value = 5, message = "MAX_CONSTRAINT_VIOLATION")
    private int rating;
}
