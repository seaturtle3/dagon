package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.compoent.CustomUserDetails;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.FileStorageService;
import kroryi.dagon.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Log4j2
public class ApiMyPageController {

    private final MyPageService myPageService;
    private final FileStorageService fileStorageService;

    // 내 정보 조회
    @PostMapping("")
    @Operation(summary = "내 정보", description = "내 정보")
    public ResponseEntity<UsersDTO> getMyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("--> {}",userDetails.getUsername());

        UsersDTO dto = myPageService.findUserInfo(userDetails.getUno()); // 서비스에서 조회
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/me")
    @Operation(summary = "이름으로 사용자 정보 조회", description = "uname으로 사용자 정보 조회")
    public ResponseEntity<UsersDTO> getUserByUname(@RequestParam String uname) {
        UsersDTO dto = myPageService.findUserInfoByUname(uname);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "내 정보 수정 ", description = "내 정보 수정")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String uname,
            @RequestParam String nickname,
            @RequestParam String email,
            @RequestParam String phone1,
            @RequestParam String phone2,
            @RequestParam String phone3,
            @RequestParam(required = false) MultipartFile profileImage
    ) {
        String fullPhone = phone1 + "-" + phone2 + "-" + phone3;
        User user = myPageService.findByUno(userDetails.getUno());  // findByUno()로 사용자 엔티티 조회

        user.setUname(uname);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(fullPhone);

        if (profileImage != null && !profileImage.isEmpty()) {
            String imagePath = fileStorageService.store(profileImage);  // 이미지 저장
            user.setProfileImg(imagePath);
        }

        myPageService.updateUser(userDetails.getUno(), user);  // 사용자 정보 업데이트
        return ResponseEntity.ok(new UsersDTO(user));  // 변경된 사용자 정보 반환
    }

    // 비밀번호 변경
    @PutMapping("/password")
    @Operation(summary = "비밀번호 변경 ", description = "비밀번호 변경")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody PasswordFormDTO form) {
        String result = myPageService.changePassword(userDetails.getUno(), form);  // uno로 변경

        if ("success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "비밀번호 변경에 실패했습니다."));
        }
    }
    // 현재 컨트롤러 클래스 내부에 아래 메서드 추가하세요.

    @GetMapping("/point")
    @Operation(summary = "포인트 확인", description = "포인트 확인")
    public ResponseEntity<?> getUserPoint(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("포인트 조회 요청 - 사용자: {}", userDetails.getUsername());

        Integer point = myPageService.getUserPoint(userDetails.getUno());  // 서비스에서 포인트 조회
        return ResponseEntity.ok(Map.of("point", point));
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴 ", description = "회원 탈퇴")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("회원 탈퇴 요청 - 사용자: {}", userDetails.getUsername());

        String result = myPageService.deleteUserAccount(userDetails.getUno());  // 사용자 탈퇴 처리
        if ("success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 완료되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "회원 탈퇴에 실패했습니다."));
        }
    }
}




