package net.thumbtack.school.notes.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.notes.validation.constraint.Min;
import net.thumbtack.school.notes.validation.constraint.Sorting;
import net.thumbtack.school.notes.validation.constraint.UserListType;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetUsersRequest {
    @Sorting
    private String sortByRating;
    @UserListType
    private String type;
    @Min(value = 0)
    private Integer from;
    @Min(value = 1)
    private Integer count;
}
