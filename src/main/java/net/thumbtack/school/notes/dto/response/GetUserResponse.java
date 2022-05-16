package net.thumbtack.school.notes.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetUserResponse {
    private int id;
    private String firstName;
    private String patronymic;
    private String lastName;
    @JsonProperty("super")
    private Boolean isSuper;
}
