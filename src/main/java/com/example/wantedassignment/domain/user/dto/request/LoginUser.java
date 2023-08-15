package com.example.wantedassignment.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUser {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;

}
