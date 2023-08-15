package com.example.wantedassignment.domain.user.service;

import com.example.wantedassignment.common.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccessTokenService {

    private final JwtProvider jwtProvider;

    public void isValidAccessToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer", "");

        if (!jwtProvider.isValidToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }
}
