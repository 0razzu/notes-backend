package net.thumbtack.school.notes.dto.request;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeregisterUserRequest {
    private String password;
}
