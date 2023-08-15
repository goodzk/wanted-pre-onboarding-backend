package com.example.wantedassignment.domain.user.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinResultDto {

    private boolean success;

    private Long message;

    public static JoinResultDto of(boolean success, Long message) {
        return JoinResultDto.builder()
                .success(success)
                .message(message)
                .build();
    }
}
