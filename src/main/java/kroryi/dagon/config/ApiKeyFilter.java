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

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

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
        String method = request.getMethod();

        log.info("Request Path = {}", path);
        log.info("Method = {}", method);

        // Swagger, 정적 자원, 로그인/회원가입 예외 처리
        return
                // api가 아니면 모두 통과
                !path.startsWith("/api/") ||
                        (path.startsWith("/api/users/register") && method.equals("POST")) ||
                        (path.startsWith("/api/auth/login") && method.equals("POST")) ||
                        (path.startsWith("/api/auth/kakao") && method.equals("POST")) ||
                        (path.startsWith("/login")) ||
                        (path.startsWith("/js/")) ||
                        (path.startsWith("/web/users/") && method.equals("POST")) ||

                        (path.startsWith("/api/multtae/") && method.equals("GET")) ||

                        // 공지사항 admin/notices 추후 삭제
                        (path.startsWith("/api/notices") && method.equals("GET")) ||
                        (path.startsWith("/api/admin/notices") && method.equals("POST")) ||
                        (path.startsWith("/api/admin/notices") && method.equals("DELETE")) ||

                        // 알림 생성 로직
                        (path.startsWith("/api/notifications") && method.equals("POST")) ||
                        // PUT - 알림 읽음 처리
                        (path.matches("/api/notifications/.*/read") && method.equals("PUT")) ||
                        // GET - 특정 알림 조회
                        (path.matches("/api/notifications/.+") && method.equals("GET")) ||
                        // DELETE - 특정 알림 삭제
                        (path.matches("/api/notifications/.+") && method.equals("DELETE")) ||
                        // GET - 특정 유저의 알림 조회
                        (path.matches("/api/notifications/user/.+") && method.equals("GET")) ||
// GET - 이름 전화번호 유저 아이디 조회
                        (path.matches("/api/admin/register") && method.equals("POST")) ||
                        // GET - 특정 유저 수정
                        (path.matches("/api/admin/.+") && method.equals("PUT")) ||
                        // DELETE - 특정 유저 아이디 삭제
                        (path.matches("/api/admin/.+") && method.equals("DELETE")) ||
                        // GET - 특정 유저  상세조회
                        (path.matches("/api/admin/.+") && method.equals("GET")) ||

                        (path.matches("/api/admin") && method.equals("GET")) ||



                        (path.startsWith("/api/users/me") && method.equals("GET")) ||
                        // GET - 이름으로 유저 개인정보  조회
                        (path.matches("/api/mypage/me") && method.equals("GET")) ;





    }
}