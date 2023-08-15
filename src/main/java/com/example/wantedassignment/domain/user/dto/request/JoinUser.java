package com.example.wantedassignment.domain.user.dto.request;


import com.example.wantedassignment.domain.user.entity.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class JoinUser {

    @Email
    private String email;

    @Size(min = 8)
    private String password;

    @Size(min = 2, max = 10)
    private String name;

    @Builder
    public JoinUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User toEntity(JoinUser joinUserDto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(joinUserDto.getEmail())
                .password(passwordEncoder.encode(joinUserDto.getPassword()))
                .name(joinUserDto.getName())
                .build();
    }
}
