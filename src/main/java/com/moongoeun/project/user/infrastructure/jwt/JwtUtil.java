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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
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
        validateToken(refreshToken);
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

    public void validateToken(String token) {
        try {
            getClaimsFrom(removeBearerPrefix(token));
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new CustomException(AuthExceptionCode.EXPIRED_TOKEN);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(AuthExceptionCode.INVALID_SIGNATURE);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(AuthExceptionCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomException(AuthExceptionCode.EMPTY_CLAIMS);
        }
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
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public String getUserIdFrom(String token) {
        return getClaimsFrom(token).get(JwtClaim.USER_ID.getName(), String.class);
    }

    public RoleType[] getRolesFrom(String token) {
        List<String> roleList = getClaimsFrom(token).get(JwtClaim.ROLES.getName(), List.class);
        return roleList.stream()
            .map(role -> RoleType.valueOf(role.replace("ROLE_", "")))
            .toArray(RoleType[]::new);
    }
}
