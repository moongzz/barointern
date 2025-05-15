package com.moongoeun.project.user.infrastructure.jwt;

import com.moongoeun.project.global.exception.CustomException;
import com.moongoeun.project.user.application.dto.response.ResAuthGetRefreshDTOApiV1;
import com.moongoeun.project.user.application.exception.AuthExceptionCode;
import com.moongoeun.project.user.domain.vo.JwtClaim;
import com.moongoeun.project.user.domain.vo.RoleType;
import com.moongoeun.project.user.domain.vo.TokenExpiration;
import com.moongoeun.project.user.infrastructure.userdetails.UserDetailsImpl;
import com.moongoeun.project.user.infrastructure.userdetails.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    public static final String BEARER_PREFIX = "Bearer ";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final UserDetailsServiceImpl userDetailsService;
    private final SecretKey secretKey;

    public JwtUtil(
        UserDetailsServiceImpl userDetailsService,
        @Value("${jwt.secret.key}") String secretKey
    ) {
        this.userDetailsService = userDetailsService;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createAccessToken(Long userId, RoleType[] roles, String nickname) {
        String token = createToken(
            "ACCESS",
            userId,
            roles,
            nickname,
            TokenExpiration.ACCESS_TOKEN.getSeconds()
        );
        return addBearerPrefix(token);
    }

    public String createRefreshToken(String accessToken) {
        Long userId = Long.parseLong(extractSubject(removeBearerPrefix(accessToken)));
        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserById(userId);

        return createToken(
            "REFRESH",
            userId,
            user.getRoles(),
            user.getNickname(),
            TokenExpiration.REFRESH_TOKEN.getSeconds()
        );
    }

    public ResAuthGetRefreshDTOApiV1 validateRefreshToken(String accessToken, String refreshToken) {
        String accessTokenSubject = extractSubject(accessToken);
        Claims refreshTokenClaims = getClaimsFrom(refreshToken);

        if (!accessTokenSubject.equals(refreshTokenClaims.getSubject())) {
            throw new CustomException(AuthExceptionCode.REFRESH_TOKEN_SUBJECT_MISMATCH);
        }

        if (isTokenExpired(refreshTokenClaims.getExpiration())) {
            throw new CustomException(AuthExceptionCode.EXPIRED_TOKEN);
        }

        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserById(Long.parseLong(accessTokenSubject));
        String newAccessToken = createAccessToken(user.getId(), user.getRoles(), user.getNickname());
        return ResAuthGetRefreshDTOApiV1.of(newAccessToken);
    }

    public String createToken(
        String category,
        Long userId,
        RoleType[] roles,
        String nickname,
        Long expiresIn
    ) {
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim(JwtClaim.CATEGORY.getName(), category)
            .claim(JwtClaim.USER_ID.getName(), userId.toString())
            .claim(JwtClaim.ROLES.getName(), roles)
            .claim(JwtClaim.NICKNAME.getName(), nickname)
            .setExpiration(getExpiryDateFromNow(expiresIn))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(secretKey, signatureAlgorithm)
            .compact();
    }

    private Date getExpiryDateFromNow(Long expiresIn) {
        return new Date(System.currentTimeMillis() + expiresIn * 1000);
    }

    private String addBearerPrefix(String token) {
        return BEARER_PREFIX + token;
    }

    private String extractSubject(String token) {
        try {
            return getClaimsFrom(token).getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    private String removeBearerPrefix(String token) {
        return (token != null && token.startsWith(BEARER_PREFIX)) ? token.substring(
            BEARER_PREFIX.length()) : token;
    }

    private Claims getClaimsFrom(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthExceptionCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new CustomException(AuthExceptionCode.INVALID_JWT_TOKEN);
        }
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
