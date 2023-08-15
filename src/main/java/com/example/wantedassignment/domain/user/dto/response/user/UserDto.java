package com.example.wantedassignment.domain.user.dto.response.user;

import com.example.wantedassignment.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long userId;

    private String email;

    private String name;

    private String role;

    @Builder
    public UserDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRoles();
    }
}
