package com.example.wantedassignment.common.filter;

import com.example.wantedassignment.common.util.CookieProvider;
import com.example.wantedassignment.common.util.JwtProvider;
import com.example.wantedassignment.domain.user.dto.request.LoginUser;
import com.example.wantedassignment.domain.user.dto.response.user.UserDto;
import com.example.wantedassignment.domain.user.service.RefreshTokenService;
import com.example.wantedassignment.domain.user.service.UserService;
import com.example.wantedassignment.domain.user.service.UserService.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    private final Environment env;

    @Builder
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,
                                      RefreshTokenService refreshTokenService, Environment env) {
        this.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.env = env;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        log.info("SUCCESSFUL Authentication");
        CookieProvider cookieProvider = new CookieProvider(env);
        JwtProvider jwtProvider = new JwtProvider(env);

        String email = ((CustomUserDetails) authResult.getPrincipal()).getUsername();
        UserDto user = userService.getUserByEmail(email);
        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken();

        refreshTokenService.updateRefreshToken(user.getUserId(), jwtProvider.getRefreshTokenId(refreshToken));

        ResponseCookie resCookie = cookieProvider.createRefreshTokenCookie(refreshToken);
        Cookie cookie = cookieProvider.of(resCookie);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(cookie);

        Map<String, Object> token = Map.of(
                "access-token", accessToken,
                "expired-time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(jwtProvider.getExpiredTime(accessToken))
        );

        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginUser creds = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            UserDetails user = userService.loadUserByUsername(creds.getEmail());
            log.info(user.getUsername());
            return getAuthenticationManager()
                    .authenticate(
                    new UsernamePasswordAuthenticationToken(user, creds.getPassword(), user.getAuthorities())
            );
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] LOGIN FAIL", e);
        }
    }
}
