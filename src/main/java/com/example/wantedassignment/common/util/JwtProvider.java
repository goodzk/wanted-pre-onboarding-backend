package com.example.wantedassignment.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {

    private final Environment env;

    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(createExpireDate(Long.parseLong(
                        Objects.requireNonNull(env.getProperty("token.access-expired-time")))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .claim("uuid", UUID.randomUUID())
                .setExpiration(createExpireDate(Long.parseLong(
                        Objects.requireNonNull(env.getProperty("token.refresh-expired-time")))))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Claims accessClaims = getClaimsFormToken(token);
            log.info("Access expireTime: " + accessClaims.getExpiration());
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token is already expired");
            return false;
        } catch (JwtException e) {
            log.error("Token is tampered");
            return false;
        } catch (NullPointerException e) {
            log.error("Token is null");
            return false;
        }
    }

    private Date createExpireDate(long expireDate) {
        long curTime = System.currentTimeMillis();
        return new Date(curTime + expireDate);
    }

    private Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpiredTime(String token) {
        return getClaimsFormToken(token).getExpiration();
    }

    public String getUserId(String token) {
        return getClaimsFormToken(token).getSubject();
    }

    public String getRefreshTokenId(String token) {
        return getClaimsFormToken(token).get("uuid").toString();
    }
}
