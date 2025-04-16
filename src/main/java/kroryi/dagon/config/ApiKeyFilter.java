package kroryi.dagon.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kroryi.dagon.compoent.CustomUserDetails;
import kroryi.dagon.service.ApiKeyService;
import kroryi.dagon.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Log4j2
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;
    private final JwtUtil jwtTokenUtil;

    public ApiKeyFilter(ApiKeyService apiKeyService, JwtUtil jwtTokenUtil) {
        this.apiKeyService = apiKeyService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("ApiKeyFilter--------------------------->");
        String authHeader = request.getHeader("Authorization");
        log.info("------------------- {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                Claims claims = jwtTokenUtil.parseToken(jwt); // ← 여기서 클레임 꺼냄

                Long uno = Long.valueOf(claims.get("uno", String.class)); // JWT에 uno 들어 있어야 함
                String uname = claims.get("uname", String.class);

                // CustomUserDetails 생성
                CustomUserDetails userDetails = new CustomUserDetails(
                        uno,
                        uname,
                        "", // 비밀번호는 필요 없으면 빈 문자열로
                        List.of(new SimpleGrantedAuthority("ROLE_PARTNER")) // 또는 적절한 권한
                );

                // 인증 객체 생성 및 SecurityContext 등록
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
                return;

            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT: " + e.getMessage());
                return;
            }
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Unauthorized: Missing or invalid Bearer token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod(); // ← HTTP 메서드 가져오기

        // Swagger 관련 경로 제외
        return !path.startsWith("/api/") ||
                (path.startsWith("/web/users/") && method.equals("POST")) ||
                (path.startsWith("/api/users/register") && method.equals("POST")) ||
                (path.startsWith("/api/auth/login") && method.equals("POST")) ||
                (path.startsWith("/api/multtae/") && method.equals("GET")) ||
                (path.startsWith("/api/product/all") && method.equals("GET"));
    }
}