package kroryi.dagon.controller.api;

import kroryi.dagon.DTO.AuthResponseDTO;
import kroryi.dagon.DTO.LoginRequestDTO;
import kroryi.dagon.entity.ApiKeyEntity;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // 추가
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class ApiAuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 주입
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUid(loginRequestDTO.getUid());

        log.info("111111111111 {}", optionalUser.isPresent());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("아이디가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        User user = optionalUser.get();

        // 입력된 비밀번호를 암호화하여 데이터베이스 비밀번호와 비교
        if (passwordEncoder.matches(loginRequestDTO.getUpw(), user.getUpw())) {

            String token = jwtUtil.generateToken(loginRequestDTO);
            log.info("token {}", token);

            return ResponseEntity.ok(new AuthResponseDTO(token, "로그인 성공"));
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}