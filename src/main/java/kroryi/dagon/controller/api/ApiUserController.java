package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.LoginRequestDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.UserService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
public class ApiUserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    @Operation(summary = "로그인 ", description = "로그인")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. JWT 추출
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if (token == null) {
            return new ResponseEntity<>("JWT 토큰이 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 2. 토큰 파싱
        String uid = jwtUtil.getUidFromToken(token);
        log.info("uid---->: " + uid);

        if (uid == null) {
            return new ResponseEntity<>("유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }

        // 3. 사용자 조회
        Optional<User> optionalUser = userRepository.findByUid(uid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();
        log.info("user-----> {}", user);


        // 4. 유저가 비활성화 상태인 경우 로그인 거부


        // 5. 응답
        return ResponseEntity.ok(new UserInfoResponseDTO(user.getUno(), user.getDisplayName()));
    }

    record UserInfoResponseDTO(Long uno, String displayName) {}



    @GetMapping("/find-id")
    @Operation(summary = "아이디 조회 ", description = "전화번호 이름으로 아이디 조회")
    public ResponseEntity<?> findUserId(@RequestParam String phone, @RequestParam String uname) {
        // 1. 전화번호와 이름으로 사용자 정보 조회
        Optional<User> optionalUser = userRepository.findByPhoneAndUname(phone, uname);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("전화번호와 이름에 해당하는 사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
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
