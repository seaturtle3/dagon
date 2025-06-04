package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.FindPasswordRequestDTO;
import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.PartnerService;
import kroryi.dagon.service.image.FileStorageService;
import kroryi.dagon.service.pages.user.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
// J : ⚠ 실제 경로는 /api/find-password 지만 인증 기능이라 auth 폴더에 위치시킴
@Tag(name = "Auth", description = "로그인, 회원가입, 비밀번호 찾기 등 인증 관련 API")
public class ApiAuthPasswordController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyPageService myPageService;
    private final FileStorageService fileStorageService;
    private final PartnerService partnerService;

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

    @PutMapping("/password")
    @Operation(summary = "비밀번호 변경 ", description = "비밀번호 변경")
    public ResponseEntity<?> changePasswordWithoutToken(@RequestBody PasswordFormDTO form) {
        // form 안에 userId (또는 username) 있어야 함
        String userId = form.getUserId();
        String result = myPageService.changePassword(Long.valueOf(userId), form);

        if ("success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "비밀번호 변경에 실패했습니다."));
        }
    }
}

