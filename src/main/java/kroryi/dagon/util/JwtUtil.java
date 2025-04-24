package kroryi.dagon.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kroryi.dagon.DTO.LoginRequestDTO;
import kroryi.dagon.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Log4j2
@Component
public class JwtUtil {

    @Value("${jwt.secret}") // application.properties 등에 jwt.secret 설정 필요
    private String secret;

    @Value("${jwt.expiration}") // 토큰 유효 시간 설정 (밀리초)
    private long expirationTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String uid, Long uno, String uname, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(uid)
                .claim("uno", uno.toString())
                .claim("uname", uname)
                .claim("role",role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }



    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT expired", e);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT", e);
        }
    }
    // 토큰 검증 및 정보 추출 등의 메서드 추가 필요
    // JWT 토큰에서 uid (Subject) 추출
    public String getUidFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (JwtException e) {
            // 토큰 파싱 실패 시 null 또는 예외 처리
            log.error("JWT 파싱 실패: {}", e.getMessage());
            return null;
        }
    }


    // isValidToken: 토큰이 유효한지 확인하는 메서드
    public boolean isValidToken(String token) {
        try {
            Claims claims = parseToken(token);
            // 토큰이 유효한 경우 만료일이 지나지 않았는지 체크
            return !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            // 유효하지 않거나 만료된 토큰일 경우 false 리턴
            log.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

}

