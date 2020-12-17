package net.thumbtack.school.notes.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GetUsersResponseItem {
    private int id;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String login;
    private LocalDateTime timeRegistered;
    private boolean online;
    private boolean deleted;
    @JsonProperty("super")
    private Boolean isSuper;
    private double rating;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetUsersResponseItem)) return false;
        GetUsersResponseItem that = (GetUsersResponseItem) o;
        return getId() == that.getId();
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
