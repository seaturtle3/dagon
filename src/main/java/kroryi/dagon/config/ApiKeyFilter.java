package kroryi.dagon.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.service.ApiKeyService;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.util.JwtUtil;
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
                Claims claims = jwtTokenUtil.parseToken(jwt); // JWT 파싱

                String role = claims.get("role", String.class);
                String uid = claims.getSubject(); // ✅ sub 필드에서 꺼냄
                String uname = claims.get("uname", String.class); // 이름

                if (role == null || uid == null) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("Invalid JWT: missing role or uid");
                    return;
                }

                // 권한 설정
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                );

                Object principal;

                if ("ADMIN".equalsIgnoreCase(role))  {
                    // 🔵 관리자일 경우 AdminUserDetails 사용
                    principal = new AdminUserDetails(uid, role);
                } else {
                    // 🔵 사용자일 경우 uno 필요
                    Integer unoInt = claims.get("uno", Integer.class);
                    if (unoInt == null) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("Invalid JWT: missing uno for USER");
                        return;
                    }

                    Long uno = unoInt.longValue();
                    principal = new CustomUserDetails(uno, uid, "", authorities, role);

                }
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT: " + e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);

//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.getWriter().write("Unauthorized: Missing or invalid Bearer token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("Request Path = {}", path);
        log.info("Method = {}", method);

        // 정적 리소스 및 Swagger
        if (path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.equals("/api-docs") ||
                path.equals("/api-docs/swagger-config") ||
                path.equals("/favicon.ico") ||
                path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/img/")) {
            return true;
        }

        // 로그인, 회원가입 관련
        if (path.equals("/login") || path.equals("/admin/login") || path.equals("/register") ||
                path.startsWith("/login/oauth2") ||
                (path.equals("/api/auth/login") && method.equals("POST")) ||
                (path.equals("/api/users/register") && method.equals("POST")) ||
                (path.startsWith("/web/users/") && method.equals("POST"))) {
            return true;
        }

        // 공개 API 허용 목록
        if ((path.equals("/api/users/me") && method.equals("GET")) ||
                (path.equals("/api/mypage/me") && method.equals("GET")) ||
                (path.startsWith("/api/multtae") && method.equals("GET")) ||
                (path.equals("/multtae") || path.startsWith("/multtae/")) ||
                (path.equals("/api/admin/station") && method.equals("POST")) ||
                (path.equals("/admin/registration") && method.equals("GET"))) {
            return true;
        }

        if (path.equals("/api/find-password") && method.equals("POST")) {
            return true;
        }

        // 공지사항/FAQ/이벤트 조회 API는 허용 (읽기 전용)admin/faq-categories
        if ((path.startsWith("/api/notices") || path.startsWith("/api/event") || path.startsWith("/api/faq")) &&
                method.equals("GET")) {
            return true;
        }

        // 알림 API 허용 목록
        if ((path.startsWith("/api/notifications") && method.equals("POST")) ||
                (path.matches("/api/notifications/.*/read") && method.equals("PUT")) ||
                (path.matches("/api/notifications/.+") && (method.equals("GET") || method.equals("DELETE"))) ||
                (path.matches("/api/notifications/user/.+") && method.equals("GET"))) {
            return true;
        }

        // 상품 조회/생성 허용
        if ((path.matches("/api/product/getAll") && method.equals("GET")) ||
                (path.matches("/api/product/get/.+") && method.equals("GET")) ||
                (path.matches("/api/product/create") && method.equals("POST"))) {
            return true;
        }

        // 조황 정보 조회/등록 허용
        if ((path.matches("/api/fishing-report/get-all") && method.equals("GET")) ||
                (path.matches("/api/fishing-report/get/.+") && method.equals("GET")) ||
                (path.matches("/api/fishing-reports") && method.equals("GET")) ||
                (path.matches("/api/fishing-report/create") && method.equals("POST"))) {
            return true;
        }

        // 예약 전체 조회
        if ((path.equals("/api/reservation/all") && method.equals("GET")) ||
                (path.equals("/api/reservation/get") && method.equals("GET"))) {
            return true;
        }

        // 관리자 회원가입 관련
        if ((path.equals("/api/admin/register") && method.equals("POST")) ||
                (path.equals("/api/admin/login") && method.equals("POST"))) {
            return true;
        }

        if (path.equals("/admin/api-keys/new") && method.equals("POST")) {
            return true;
        }

        // api가 아닌 경로는 필터 적용하지 않음
        return !path.startsWith("/api/");
    }
}