package com.example.wantedassignment.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("refresh_token")
@NoArgsConstructor
public class Token {

    @Id
    private String userId;

    private String tokenId;

    public Token(String userId, String tokenId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }
}
