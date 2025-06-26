package kroryi.dagon.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.LoginRequestDTO;
import kroryi.dagon.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.Map;

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

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(user.getUid())
                .claim("uno", user.getUno())
                .claim("uname", user.getUname())
                .claim("nickname", user.getNickname())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Admin용 JWT 토큰 생성
     */
    public String generateToken(kroryi.dagon.entity.Admin admin) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(admin.getAid())
                .claim("aid", admin.getAid())
                .claim("aname", admin.getAname())
                .claim("role", admin.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAdminToken(String aid, String aname, String role, Long uno) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(aid)// ✅ 추가
                .claim("aid", aid)
                .claim("aname", aname)
                .claim("role", role)
                .claim("uno", uno)
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
            log.error("JWT 토큰이 만료되었습니다: {}", e.getMessage());
            throw new JwtException("JWT expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 형식입니다: {}", e.getMessage());
            throw new JwtException("Unsupported JWT", e);
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 형식입니다: {}", e.getMessage());
            throw new JwtException("Malformed JWT", e);
        } catch (SecurityException e) {
            log.error("JWT 서명 검증 실패: {}", e.getMessage());
            throw new JwtException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 비어있거나 null입니다: {}", e.getMessage());
            throw new JwtException("Empty or null JWT", e);
        } catch (JwtException e) {
            log.error("JWT 파싱 중 알 수 없는 오류: {}", e.getMessage());
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

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean isAdmin(String token) {
        try {
            Claims claims = parseToken(token);
            String role = claims.get("role", String.class);
            return "ADMIN".equalsIgnoreCase(role);
        } catch (Exception e) {
            log.error("어드민 체크 중 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    public Long getUnoFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Object unoObj = claims.get("uno");
            
            if (unoObj == null) {
                log.error("JWT 토큰에 uno claim이 없습니다");
                throw new JwtException("Missing uno claim in JWT");
            }
            
            Long uno;
            if (unoObj instanceof Number) {
                uno = ((Number) unoObj).longValue();
            } else if (unoObj instanceof String) {
                uno = Long.parseLong((String) unoObj);
            } else {
                log.error("uno claim의 타입이 예상과 다릅니다: {}", unoObj.getClass().getName());
                throw new JwtException("Invalid uno claim type in JWT");
            }
            
            log.debug("JWT에서 추출한 uno: {}", uno);
            return uno;
            
        } catch (JwtException e) {
            log.error("JWT에서 uno 추출 실패: {}", e.getMessage());
            throw e;
        } catch (NumberFormatException e) {
            log.error("uno claim을 Long으로 변환할 수 없습니다: {}", e.getMessage());
            throw new JwtException("Invalid uno claim format in JWT", e);
        }
    }

    // JWT 토큰에서 uname 추출 (일반 사용자용)
    public String getUnameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            String uname = claims.get("uname", String.class);
            if (uname == null) {
                log.warn("JWT 토큰에 uname claim이 없습니다");
                return null;
            }
            log.debug("JWT에서 추출한 uname: {}", uname);
            return uname;
        } catch (JwtException e) {
            log.error("JWT에서 uname 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // JWT 토큰에서 aname 추출 (관리자용)
    public String getAnameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            String aname = claims.get("aname", String.class);
            if (aname == null) {
                log.warn("JWT 토큰에 aname claim이 없습니다");
                return null;
            }
            log.debug("JWT에서 추출한 aname: {}", aname);
            return aname;
        } catch (JwtException e) {
            log.error("JWT에서 aname 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // JWT 토큰에서 사용자 이름 추출 (역할에 따라 자동 선택)
    public String getUserNameFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            String role = claims.get("role", String.class);
            
            if ("ADMIN".equalsIgnoreCase(role) || "SUPER_ADMIN".equalsIgnoreCase(role)) {
                return getAnameFromToken(token);
            } else {
                return getUnameFromToken(token);
            }
        } catch (JwtException e) {
            log.error("JWT에서 사용자 이름 추출 실패: {}", e.getMessage());
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

    // 파트너 위임 토큰 생성 (관리자가 파트너 권한으로 접근)
    public String generatePartnerImpersonationToken(String uid, String uname, String role, Long partnerUno, Long originalAdminUno) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(uid)
                .claim("uid", uid)
                .claim("uname", uname)
                .claim("role", role)
                .claim("uno", partnerUno)
                .claim("originalAdminUno", originalAdminUno) // 원본 관리자 uno 저장
                .claim("isImpersonated", true) // 위임 토큰임을 표시
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 위임 토큰에서 원본 관리자 uno 추출
    public Long getOriginalAdminUnoFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            Object originalAdminUnoObj = claims.get("originalAdminUno");
            if (originalAdminUnoObj != null) {
                return ((Number) originalAdminUnoObj).longValue();
            }
            return null;
        } catch (JwtException e) {
            log.error("위임 토큰에서 원본 관리자 uno 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // 위임 토큰인지 확인
    public boolean isImpersonatedToken(String token) {
        try {
            Claims claims = parseToken(token);
            Boolean isImpersonated = claims.get("isImpersonated", Boolean.class);
            return isImpersonated != null && isImpersonated;
        } catch (JwtException e) {
            log.error("위임 토큰 확인 실패: {}", e.getMessage());
            return false;
        }
    }

}

