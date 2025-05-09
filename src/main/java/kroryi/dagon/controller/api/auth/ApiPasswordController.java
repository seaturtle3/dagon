package kroryi.dagon.controller.api.auth;

import kroryi.dagon.DTO.FindPasswordRequestDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiPasswordController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordRequestDTO request) {

        Optional<User> optionalUser = userRepository.findByUidAndUnameAndEmail(
                request.getUid(), request.getUname(), request.getEmail()
        );

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일치하는 사용자가 없습니다.");
        }

        User user = optionalUser.get();

        // 임시 비밀번호 생성
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setUpw(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("tempPassword", tempPassword);

        return ResponseEntity.ok(response);
    }
}

