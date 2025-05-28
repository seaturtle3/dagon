package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.AuthResponseDTO;
import kroryi.dagon.DTO.LoginRequestDTO;
import kroryi.dagon.entity.ApiKeyEntity;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.ApiKeyRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // 추가
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Auth", description = "로그인, 로그아웃, 인증 관련 API")
public class ApiAuthLoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 주입
    private final JwtUtil jwtUtil;
    private final ApiKeyRepository apiKeyRepository;


    @PostMapping("/login")
    @Operation(summary = "로그인 ", description = "로그인")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUid(loginRequestDTO.getUid());

        log.info("111111111111 {}", optionalUser.isPresent());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("아이디가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        User user = optionalUser.get();

        if (!user.isActive()) {
            return new ResponseEntity<>("비활성화된 계정입니다. 로그인할 수 없습니다.", HttpStatus.FORBIDDEN);  // 비활성화된 유저는 로그인 불가
        }

        // 입력된 비밀번호를 암호화하여 데이터베이스 비밀번호와 비교
        if (passwordEncoder.matches(loginRequestDTO.getUpw(), user.getUpw())) {
            String token = jwtUtil.generateToken(user);


            log.info("token {}", token);

            return ResponseEntity.ok(new AuthResponseDTO(token, "로그인 성공"));
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenHeader) {
        // Bearer 제거
        String token = tokenHeader.replace("Bearer ", "");

        ApiKeyEntity apiKey = apiKeyRepository.findByKey(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "API Key가 존재하지 않습니다."));

        if (!apiKey.getActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 로그아웃된 토큰입니다.");
        }

        apiKey.setActive(false);
        apiKey.setExpiresAt(LocalDateTime.now()); // 만료 즉시 적용
        apiKeyRepository.save(apiKey);

        return ResponseEntity.ok("로그아웃 성공");
    }
}