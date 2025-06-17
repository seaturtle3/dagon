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
        String encodedPw = memberSecurityDTO.getMpw();

        // 비밀번호가 1111이어도 무조건 JWT 발급
        String email = memberSecurityDTO.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        String jwt = jwtUtil.generateToken(user);

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            // API 방식: JSON으로 토큰 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"authToken\": \"" + jwt + "\", \"message\": \"로그인 성공\"}");
        } else {
            // 웹(Thymeleaf) 방식: my-page로 토큰을 쿼리파라미터로 전달하며 리다이렉트
            response.sendRedirect("/my-page?token=" + jwt);
        }
    }
}
