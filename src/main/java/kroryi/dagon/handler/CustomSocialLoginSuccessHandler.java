package kroryi.dagon.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.dagon.DTO.MemberSecurityDTO;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.entity.User;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Value("${app.server.base-url}")
    private String baseUrl;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        log.info("------------- 소셜 로그인 성공 처리 -----------");
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        
        // 사용자 정보 조회
        String email = memberSecurityDTO.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        
        // JWT 토큰 생성
        String jwt = jwtUtil.generateToken(user);
        log.info("Generated JWT token: {}", jwt);

        // state 파라미터에서 baseUrl 추출
        String baseUrl = request.getParameter("state");

        // 허용된 baseUrl 목록
        java.util.List<String> allowedBaseUrls = java.util.List.of(
            "http://docs.yi.or.kr:8097",
            "http://docs.yi.or.kr:8095",
            "http://localhost:8095",
            "http://localhost:8097",
            "http://localhost:5173"
            // 필요시 추가
        );

        // baseUrl 검증 및 기본값 처리
        if (baseUrl == null || allowedBaseUrls.stream().noneMatch(baseUrl::equals)) {
            baseUrl = this.baseUrl;
        }

        // baseUrl에 경로 및 파라미터 추가
        String redirectUrl = "http://docs.yi.or.kr:25173/" + "oauth/callback?token=" + jwt + "&success=true";

        log.info("Redirecting to frontend: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
