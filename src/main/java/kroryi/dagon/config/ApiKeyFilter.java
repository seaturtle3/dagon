package kroryi.dagon.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kroryi.dagon.service.ApiKeyService;
import kroryi.dagon.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        String authHeader = request.getHeader("Authorization");
        log.info("--------->{}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            log.info("jwt ---------------{}", jwt);

            try {
                Claims claims = jwtTokenUtil.parseToken(jwt);
                log.info("jwt2 ---------------{}", claims);
                String keyId = claims.getSubject();
                String clientIp = request.getRemoteAddr();
                String callbackUrl = request.getHeader("callbackUrl");

                log.info("keyId--------->{}", keyId);
                log.info("clientIp--------->{}", clientIp);
                log.info("callbackUrl--------->{}", callbackUrl);


                boolean isValid = apiKeyService.isValidKey(jwt, clientIp, callbackUrl);
                if (isValid) {
                    // 인증 객체 설정 (선택사항)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(keyId, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT: " + e.getMessage());
                return;
            }
        }

        // 인증 실패
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Unauthorized: Missing or invalid Bearer token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Swagger 관련 경로 제외
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui/index.html") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars") ||
                path.startsWith("/") ||
                path.startsWith("/register") ||
                path.startsWith("/js/**") ||
                path.startsWith("/css/**") ||
                path.startsWith("/img/**") ||
                path.startsWith("/images/**") ||
                path.startsWith("/uploads/**") ||
                path.startsWith("/uploadImage/**") ||
                path.startsWith("/api/**") ||
                path.startsWith("/reservation.html") ||
                path.equals("/error") ||
                path.equals("/*") ||
                path.equals("/admin/api-keys"); // ✅ 여기를 추가!

    }
}