package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "OAuth", description = "OAuth 인증 관련 API")
public class ApiOAuthController {

    @GetMapping("/callback")
    @Operation(summary = "OAuth 콜백 처리", description = "프론트엔드에서 OAuth 콜백을 처리합니다")
    public String handleOAuthCallback(@RequestParam(required = false) String token,
                                     @RequestParam(required = false) String success,
                                     @RequestParam(required = false) String error) {
        
        log.info("OAuth callback received - token: {}, success: {}, error: {}", token, success, error);
        
        if (error != null) {
            log.error("OAuth error: {}", error);
            return "OAuth 인증에 실패했습니다: " + error;
        }
        
        if (success != null && token != null) {
            log.info("OAuth success with token");
            return "OAuth 인증이 성공했습니다. 토큰이 발급되었습니다.";
        }
        
        return "OAuth 콜백이 처리되었습니다.";
    }

    @GetMapping("/status")
    @Operation(summary = "OAuth 상태 확인", description = "OAuth 인증 상태를 확인합니다")
    public String checkOAuthStatus() {
        return "OAuth 서비스가 정상적으로 작동 중입니다.";
    }
} 