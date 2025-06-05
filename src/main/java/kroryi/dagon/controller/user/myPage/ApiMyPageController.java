package kroryi.dagon.controller.user.myPage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.PasswordFormDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.UserLevel;
import kroryi.dagon.service.auth.PartnerService;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.service.image.FileStorageService;
import kroryi.dagon.service.pages.user.MyPageService;
import kroryi.dagon.util.LevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Auth", description = "로그인, 마이페이지, 계정 수정, 포인트 등 인증 관련 API")
public class ApiMyPageController {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    private final MyPageService myPageService;
    private final FileStorageService fileStorageService;
    private final PartnerService partnerService;

    //     내 정보 조회
    @PostMapping("")
    @Operation(summary = "내 정보", description = "내 정보")
    public ResponseEntity<UsersDTO> getMyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("--> {}", userDetails.getUname());

        UsersDTO dto = myPageService.findUserInfo(userDetails.getUno()); // 서비스에서 조회

        log.info("----------> {}", dto.getProfile_image());

        if (dto.getProfile_image() == null || dto.getProfile_image().isEmpty()) {
            dto.setProfile_image("/img/default-profile.png");  // 정적 리소스 경로
        }

        Partner partner = partnerService.findPartnerByUserUno(userDetails.getUno());
        if (partner != null) {
            dto.setPname(partner.getPname());
            dto.setCeoName(partner.getCeoName());
            dto.setPAddress(partner.getPAddress());
            dto.setPInfo(partner.getPInfo());
            dto.setLicense(partner.getLicense());
            dto.setLicenseImg(partner.getLicenseImg());
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    @Operation(summary = "이름으로 사용자 정보 조회", description = "uname으로 사용자 정보 조회")
    public ResponseEntity<UsersDTO> getUserByUname(@RequestParam String uname) {
        UsersDTO dto = myPageService.findUserInfoByUname(uname);


        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "내 정보 수정", description = "내 정보 수정")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String uname,
            @RequestParam String nickname,
            @RequestParam String email,
            @RequestParam String phone1,
            @RequestParam String phone2,
            @RequestParam String phone3,
            @RequestParam(required = false) MultipartFile profileImage
    ) throws IOException {

        log.info("수정 요청한 사용자 정보: {}", userDetails);
        String fullPhone = phone1 + "-" + phone2 + "-" + phone3;
        User user = myPageService.findByUno(userDetails.getUno());  // 사용자 조회

        user.setUname(uname);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(fullPhone);

        if (profileImage != null && !profileImage.isEmpty()) {
            String originalFilename = profileImage.getOriginalFilename();
            String safeFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

            Path savePath = Paths.get(uploadDir, safeFilename);
            Files.copy(profileImage.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfileImg(safeFilename);  // 이미지 파일명 저장 (또는 전체 경로 저장도 가능)
        }

        myPageService.updateUser(userDetails.getUno(), user);  // 정보 저장
        return ResponseEntity.ok(new UsersDTO(user));  // DTO 반환
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
    @Operation(summary = "포인트 및 레벨 확인", description = "포인트 및 레벨 확인")
    public ResponseEntity<?> getUserPoint(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("포인트 및 레벨 조회 요청 - 사용자: {}", userDetails.getUsername());

        Map<String, Object> result = myPageService.getUserPointAndLevel(userDetails.getUno());
        return ResponseEntity.ok(result);
    }


    // 회원 탈퇴
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "비밀번호 확인 후 회원 탈퇴")
    public ResponseEntity<?> deleteAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> requestBody) {

        log.info("회원 탈퇴 요청 - 사용자: {}", userDetails.getUsername());

        String inputPassword = requestBody.get("password");
        if (inputPassword == null || inputPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "비밀번호를 입력해주세요."));
        }

        boolean isValid = myPageService.verifyPassword(userDetails.getUno(), inputPassword);
        if (!isValid) {
            return ResponseEntity.badRequest().body(Map.of("error", "비밀번호가 일치하지 않습니다."));
        }

        String result = myPageService.deleteUserAccount(userDetails.getUno());
        if ("success".equals(result)) {
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 성공적으로 완료되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "회원 탈퇴에 실패했습니다."));
        }
    }
}




