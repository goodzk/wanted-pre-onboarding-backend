package com.example.wantedassignment.domain.user.controller;

import com.example.wantedassignment.domain.user.dto.response.token.JwtTokenDto;
import com.example.wantedassignment.domain.user.dto.response.token.RefreshTokenResponse;
import com.example.wantedassignment.domain.user.service.AccessTokenService;
import com.example.wantedassignment.domain.user.service.RefreshTokenService;
import com.example.wantedassignment.domain.user.service.UserService;
import com.example.wantedassignment.common.util.CookieProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final AccessTokenService accessTokenService;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    private final CookieProvider cookieProvider;

    @PatchMapping("/token")
    public ResponseEntity<?> reissueRefreshToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken,
                                          @CookieValue(name = "refresh-token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.badRequest()
                    .body("Refresh token is not valid. try to login again.");
        }

        JwtTokenDto jwtTokenDto = refreshTokenService.reissueToken(accessToken, refreshToken);
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new RefreshTokenResponse(jwtTokenDto));
    }

    @GetMapping("/token")
    public ResponseEntity<?> checkAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        accessTokenService.isValidAccessToken(authorization);
        Long userId = userService.getUserIdByToken(authorization);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userId);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        accessTokenService.isValidAccessToken(token);
        refreshTokenService.deleteTokenByLogout(token);
        ResponseCookie res = cookieProvider.removeRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, res.toString())
                .body("logout and delete cookie");
    }
}
