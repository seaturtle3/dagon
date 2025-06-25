package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
@RequestMapping("/api/users")
@Log4j2
public class ApiUserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보 조회", description = "JWT 토큰을 통해 현재 로그인한 사용자 정보를 조회합니다.")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof kroryi.dagon.component.CustomUserDetails) {
                kroryi.dagon.component.CustomUserDetails userDetails = 
                    (kroryi.dagon.component.CustomUserDetails) authentication.getPrincipal();
                
                String uid = userDetails.getUsername();
                Long uno = userDetails.getUno();
                
                log.info("현재 사용자 정보: uid={}, uno={}", uid, uno);
                
                Optional<User> optionalUser = userRepository.findByUid(uid);
                if (optionalUser.isEmpty()) {
                    return new ResponseEntity<>("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
                }
                
                User user = optionalUser.get();
                log.info("조회된 사용자: {}", user);
                
                return ResponseEntity.ok(new UserInfoResponseDTO(user.getUno(), user.getDisplayName(), user.getUid(), user.getEmail()));
            } else {
                return new ResponseEntity<>("인증된 사용자 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("사용자 정보 조회 중 오류 발생: {}", e.getMessage());
            return new ResponseEntity<>("사용자 정보 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    record UserInfoResponseDTO(Long uno, String displayName, String uid, String email) {}

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
}
