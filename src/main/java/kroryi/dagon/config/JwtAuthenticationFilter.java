package kroryi.dagon.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            
            try {
                // JWT 토큰 검증 및 파싱
                Claims claims = jwtUtil.parseToken(jwt);
                
                String role = claims.get("role", String.class);
                String subject = claims.getSubject();
                String uname = claims.get("uname", String.class);
                
                if (role == null || subject == null) {
                    log.warn("Invalid JWT: missing role or subject");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Invalid JWT: missing role or subject");
                    return;
                }
                
                // 권한 설정
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                );
                
                Object principal;
                
                if ("ADMIN".equalsIgnoreCase(role) || "SUPER_ADMIN".equalsIgnoreCase(role)) {
                    // 관리자일 경우 AdminUserDetails 사용 (ADMIN, SUPER_ADMIN 모두)
                    principal = new AdminUserDetails(subject, role);
                } else {
                    // 일반 사용자일 경우 uno 필요
                    Integer unoInt = claims.get("uno", Integer.class);
                    if (unoInt == null) {
                        log.warn("Invalid JWT: missing uno for USER");
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("Invalid JWT: missing uno for USER");
                        return;
                    }
                    
                    Long uno = unoInt.longValue();
                    principal = new CustomUserDetails(uno, subject, "", authorities, role);
                }
                
                // SecurityContext에 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT 인증 성공: user={}, role={}", subject, role);
                
            } catch (Exception e) {
                log.warn("JWT 검증 실패: {}", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT: " + e.getMessage());
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // 정적 리소스 및 Swagger
        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.equals("/api-docs") ||
                path.equals("/api-docs/swagger-config") ||
                path.equals("/favicon.ico") ||
                path.startsWith("/css/") || 
                path.startsWith("/js/") || 
                path.startsWith("/img/") ||
                path.startsWith("/uploads/")) {
            return true;
        }
        
        // 공개 API 허용 목록
        if (path.equals("/login") || 
                path.equals("/admin/login") || 
                path.equals("/register") ||
                path.startsWith("/login/oauth2") ||
                (path.equals("/api/auth/login") && method.equals("POST")) ||
                (path.equals("/api/users/register") && method.equals("POST")) ||
                (path.equals("/api/admin/register") && method.equals("POST")) ||
                (path.equals("/api/admin/register-super") && method.equals("POST")) ||
                (path.equals("/api/admin/login") && method.equals("POST")) ||
                (path.equals("/api/find-password") && method.equals("POST")) ||
                (path.equals("/admin/registration") && method.equals("GET"))) {
            return true;
        }
        
        // 공개 조회 API 허용
        if ((path.startsWith("/api/notices") || 
                path.startsWith("/api/event") || 
                path.startsWith("/api/faq")) && method.equals("GET")) {
            return true;
        }
        
        // 상품 조회 API 허용
        if ((path.matches("/api/product/getAll") && method.equals("GET")) ||
                (path.matches("/api/product/get/.+") && method.equals("GET"))) {
            return true;
        }
        
        // 조황정보 조회 API 허용
        if ((path.matches("/api/fishing-report/get-all") && method.equals("GET")) ||
                (path.matches("/api/fishing-report/get/.+") && method.equals("GET"))) {
            return true;
        }
        
        // 예약 조회 API 허용
        if ((path.equals("/api/reservation/all") && method.equals("GET")) ||
                (path.equals("/api/reservation/get") && method.equals("GET"))) {
            return true;
        }
        
        // API가 아닌 경로는 필터 적용하지 않음
        return !path.startsWith("/api/");
    }
} 