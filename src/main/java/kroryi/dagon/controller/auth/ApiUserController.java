package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
@RequestMapping("/api/users")
@Log4j2
public class ApiUserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보 조회", description = "JWT 토큰을 통해 현재 로그인한 사용자 정보를 조회합니다.")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.getPrincipal() instanceof kroryi.dagon.component.CustomUserDetails) {
                // 일반 사용자
                kroryi.dagon.component.CustomUserDetails userDetails = 
                    (kroryi.dagon.component.CustomUserDetails) authentication.getPrincipal();
                
                String uid = userDetails.getUsername();
                Long uno = userDetails.getUno();
                
                log.info("현재 사용자 정보: uid={}, uno={}", uid, uno);
                
                Optional<User> optionalUser = userRepository.findByUno(uno);
                if (optionalUser.isEmpty()) {
                    return new ResponseEntity<>("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
                }
                
                User user = optionalUser.get();
                log.info("조회된 사용자: {}", user);
                
                return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new UserInfoResponseDTO(user.getUno(), user.getDisplayName(), user.getUid(), user.getEmail()));
                
            } else if (authentication != null && authentication.getPrincipal() instanceof kroryi.dagon.service.auth.AdminUserDetails) {
                // 관리자
                kroryi.dagon.service.auth.AdminUserDetails adminDetails = 
                    (kroryi.dagon.service.auth.AdminUserDetails) authentication.getPrincipal();
                
                String aid = adminDetails.getAid();
                String aname = adminDetails.getAname();
                String role = adminDetails.getRole();
                
                log.info("현재 관리자 정보: aid={}, aname={}, role={}", aid, aname, role);
                
                return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new AdminInfoResponseDTO(aid, aname, role));
                
            } else {
                return new ResponseEntity<>("인증된 사용자 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("사용자 정보 조회 중 오류 발생: {}", e.getMessage());
            return new ResponseEntity<>("사용자 정보 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    record UserInfoResponseDTO(Long uno, String displayName, String uid, String email) {}
    record AdminInfoResponseDTO(String aid, String aname, String role) {}

    @GetMapping("/find-id")
    @Operation(summary = "아이디 조회", description = "이메일로 아이디 조회")
    public ResponseEntity<?> findUserId(@RequestParam String email) {
        // 1. 이메일로 사용자 정보 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("이메일에 해당하는 사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        // 2. 유저 아이디를 응답
        return ResponseEntity.ok(new UserIdResponseDTO(user.getUid()));
    }

    // 응답 DTO
    record UserIdResponseDTO(String uid) {}

    @PutMapping("/{uno}/deactivate")
    @Operation(summary = "유저 계정 비활성화", description = "유저 계정을 비활성화합니다.")
    public ResponseEntity<String> deactivateUser(@PathVariable Long uno) {
        log.info("유저 계정 비활성화 요청: uno = {}", uno);
        try {
            userService.deactivateUser(uno);
            return ResponseEntity.ok("유저 계정이 비활성화되었습니다.");
        } catch (RuntimeException e) {
            log.error("유저 비활성화 실패: uno = {}", uno, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{uno}/reactivate")
    @Operation(summary = "유저 계정 활성화", description = "비활성화된 유저 계정을 다시 활성화합니다.")
    public ResponseEntity<String> reactivateUser(@PathVariable Long uno) {
        log.info("유저 계정 활성화 요청: uno = {}", uno);
        try {
            userService.reactivateUser(uno);
            return ResponseEntity.ok("유저 계정이 활성화되었습니다.");
        } catch (RuntimeException e) {
            log.error("유저 활성화 실패: uno = {}", uno, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/token-info")
    @Operation(summary = "JWT 토큰 정보 조회", description = "JWT 토큰에서 직접 사용자 정보를 추출합니다.")
    public ResponseEntity<?> getTokenInfo(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return new ResponseEntity<>("Authorization 헤더가 없습니다.", HttpStatus.UNAUTHORIZED);
            }
            
            token = token.substring(7); // "Bearer " 제거
            
            // JWT 토큰 파싱
            io.jsonwebtoken.Claims claims = jwtUtil.parseToken(token);
            
            String role = claims.get("role", String.class);
            String subject = claims.getSubject();
            String uname = claims.get("uname", String.class);
            String aname = claims.get("aname", String.class);
            Object unoObj = claims.get("uno");
            
            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("role", role);
            tokenInfo.put("subject", subject);
            tokenInfo.put("uname", uname);
            tokenInfo.put("aname", aname);
            tokenInfo.put("uno", unoObj);
            
            log.info("JWT 토큰 정보: {}", tokenInfo);
            
            return ResponseEntity.ok(tokenInfo);
            
        } catch (Exception e) {
            log.error("토큰 정보 조회 중 오류 발생: {}", e.getMessage());
            return new ResponseEntity<>("토큰 정보 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
