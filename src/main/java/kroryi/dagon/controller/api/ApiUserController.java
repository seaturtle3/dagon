package kroryi.dagon.controller.api;

import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Log4j2
public class ApiUserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        // 1. Authorization 헤더에서 JWT 토큰 추출
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if (token == null) {
            return new ResponseEntity<>("JWT 토큰이 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 2. JWT 토큰 유효성 검증 및 사용자 정보 추출 (JwtUtil에 관련 메서드 필요)
        String uid = jwtUtil.getUidFromToken(token); // JwtUtil에 구현 필요

        if (uid == null) {
            return new ResponseEntity<>("유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }

        // 3. 데이터베이스에서 사용자 정보 조회
        Optional<User> optionalUser = userRepository.findByUid(uid);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        log.info("user-----> {}", user);

        // 4. 유저 번호와 닉네임을 JSON 형태로 응답
        return ResponseEntity.ok(new UserInfoResponseDTO(user.getUno(), user.getDisplayName()));
    }

    // 응답 DTO
    record UserInfoResponseDTO(Long uno, String displayName) {}
}