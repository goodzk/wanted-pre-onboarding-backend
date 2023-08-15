package com.example.wantedassignment.domain.user.service;

import com.example.wantedassignment.domain.user.dto.response.token.JwtTokenDto;
import com.example.wantedassignment.domain.user.entity.Token;
import com.example.wantedassignment.domain.user.entity.User;
import com.example.wantedassignment.domain.user.repository.TokenRedisRepository;
import com.example.wantedassignment.domain.user.repository.UserRepository;
import com.example.wantedassignment.common.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final TokenRedisRepository tokenRepository;

    @Transactional
    public void updateRefreshToken(final Long id, final String tokenId) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("user not found");
        }
        tokenRepository.save(new Token(id.toString(), tokenId));
    }

    @Transactional
    public JwtTokenDto reissueToken(final String accessToken, final String refreshToken) {
        final String userId = jwtProvider.getUserId(accessToken.replace("Bearer", ""));

        Token targetToken = tokenRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("저장된 정보가 존재하지 않습니다.")
        );

        final String targetTokenId = targetToken.getTokenId();
        String findTokenId = jwtProvider.getRefreshTokenId(refreshToken.replace("Bearer", ""));

        if (!jwtProvider.isValidToken(refreshToken.replace("Bearer", ""))
                || !targetTokenId.equals(findTokenId)) {
            tokenRepository.delete(targetToken);
            throw new RuntimeException();
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(
                () -> new RuntimeException("저장된 정보가 존재하지 않습니다."));

        final String newAccessToken = jwtProvider.generateAccessToken(user.getId());

        return JwtTokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiredDate(jwtProvider.getExpiredTime(newAccessToken))
                .build();
    }

    @Transactional
    public void deleteTokenByLogout(String accessToken) {
        final String userId = jwtProvider.getUserId(accessToken.replace("Bearer", ""));
        Token refreshToken = tokenRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("저장된 정보가 존재하지 않습니다.")
        );

        tokenRepository.delete(refreshToken);
    }
}
