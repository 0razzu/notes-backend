package net.thumbtack.school.notes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetSettingsResponse {
    private int maxNameLength;
    private int minPasswordLength;
    private int userIdleTimeout;
}
