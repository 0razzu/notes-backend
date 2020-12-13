package net.thumbtack.school.notes.dto.request;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class DeregisterUserRequest {
    private String password;
}
