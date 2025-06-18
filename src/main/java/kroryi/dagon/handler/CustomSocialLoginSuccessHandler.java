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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

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

        // 프론트엔드로 리다이렉트 (JWT 토큰을 쿼리 파라미터로 전달)
        String frontendUrl = "http://localhost:5173/oauth/callback";
        String redirectUrl = frontendUrl + "?token=" + jwt + "&success=true";
        
        log.info("Redirecting to frontend: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
