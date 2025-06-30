package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.AdminDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.AdminService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "Admin", description = "관리자 인증 및 회원 관리 API")
public class ApiAdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager adminAuthenticationManager;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping("/register")
    @Operation(summary = "관리자 회원가입 ", description = "관리자 회원가입")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminDTO adminDTO) {
        try {
            adminService.registerAdmin(adminDTO);
            return ResponseEntity.ok("관리자 회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register-super")
    @Operation(summary = "SUPER_ADMIN 회원가입", description = "SUPER_ADMIN 권한을 가진 관리자 회원가입")
    public ResponseEntity<String> registerSuperAdmin(@RequestBody AdminDTO adminDTO) {
        try {
            adminService.registerSuperAdmin(adminDTO);
            return ResponseEntity.ok("SUPER_ADMIN 회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 ", description = "관리자 로그인")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody AdminDTO adminLoginDTO) {
        try {
            Authentication authentication = adminAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(adminLoginDTO.getAid(), adminLoginDTO.getApw())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // DB에서 관리자 정보 조회
            Admin admin = adminRepository.findByAid(adminLoginDTO.getAid())
                    .orElseThrow(() -> new UsernameNotFoundException("관리자 정보 없음"));

            // 토큰 생성
            String token = jwtUtil.generateToken(admin);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "잘못된 관리자 아이디나 비밀번호입니다."));
        }
    }


    // 전체 회원 조회 (페이징)
    @GetMapping("/users")
    @Operation(summary = "전체 회원 조회 ", description = "전체 회원 조회")
    public Page<UsersDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "") String search) {
        return adminService.getAllUsers(PageRequest.of(page, size), search);
    }

    // 회원 상세 조회
    @GetMapping("/user/{uid}")
    @Operation(summary = "상세 조회 ", description = "상세 조회")
    public UsersDTO getUser(@PathVariable String uid) {
        return adminService.getUserByUid(uid);
    }

    // 회원 수정 (JSON 기반 - 기존 API 유지)
    @PutMapping("/user/{uno}/json")
    @Operation(summary = "회원 수정 (JSON)", description = "회원 정보 수정 (JSON 기반)")
    public UsersDTO updateUserJson(@PathVariable String uno, @RequestBody UsersDTO dto) {
        return adminService.updateUser(uno, dto);
    }

    // 회원 수정 (이미지 업로드 포함)
    @PutMapping(value = "/user/{uno}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 수정", description = "회원 정보 수정 (이미지 업로드 포함)")
    public ResponseEntity<?> updateUser(
            @PathVariable String uno,
            @RequestParam String uname,
            @RequestParam String nickname,
            @RequestParam String email,
            @RequestParam String phone1,
            @RequestParam String phone2,
            @RequestParam String phone3,
            @RequestParam String points,
            @RequestParam String level,
            @RequestParam String levelPoint,
            @RequestParam String loginType,
            @RequestParam String role,
            @RequestParam boolean isActive,
            @RequestParam(required = false) MultipartFile profileImage
    ) {
        try {
            log.info("회원 수정 요청 - uno: {}, uname: {}", uno, uname);
            
            // UsersDTO 생성
            UsersDTO dto = new UsersDTO();
            dto.setUname(uname);
            dto.setNickname(nickname);
            dto.setEmail(email);
            dto.setPhone1(phone1);
            dto.setPhone2(phone2);
            dto.setPhone3(phone3);
            dto.setFullPhone(phone1 + "-" + phone2 + "-" + phone3);
            dto.setPoints(Integer.parseInt(points));
            dto.setLevel(Integer.parseInt(level));
            dto.setLevelPoint(levelPoint);
            dto.setLoginType(loginType);
            dto.setRole(role);
            dto.setActive(isActive);

            // 이미지 처리
            if (profileImage != null && !profileImage.isEmpty()) {
                // 기존 이미지 삭제
                User user = userRepository.findByUno(Long.valueOf(uno)).orElse(null);
                if (user != null && user.getProfileImg() != null && !user.getProfileImg().isBlank()) {
                    String oldImg = user.getProfileImg();
                    // 기본 이미지가 아니라면 삭제
                    if (!oldImg.equals("default-profile.png")) {
                        Path oldImgPath = Paths.get(uploadDir, oldImg);
                        try {
                            Files.deleteIfExists(oldImgPath);
                        } catch (IOException e) {
                            log.warn("기존 프로필 이미지 삭제 실패: {}", oldImg);
                        }
                    }
                }

                // 새 이미지 저장
                String originalFilename = profileImage.getOriginalFilename();
                String safeFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                Path savePath = Paths.get(uploadDir, safeFilename);
                Files.copy(profileImage.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

                dto.setProfile_image(safeFilename);
                log.info("이미지 업로드 완료: {}", safeFilename);
            }

            UsersDTO updatedUser = adminService.updateUser(uno, dto);
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            log.error("회원 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("수정 실패: " + e.getMessage());
        }
    }

    // 회원 삭제
    @DeleteMapping("/user/{uno}")
    @Operation(summary = "회원 삭제 ", description = "회원 삭제")
    public ResponseEntity<?> deleteUser(@PathVariable String uno) {
        try {
            log.info("Delete->>>>>>> {}", uno);
            adminService.deleteUser(uno);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("회원 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: " + e.getMessage());
        }
    }

    // 파트너 페이지 접근을 위한 토큰 위임 (Impersonation)
    @PostMapping("/impersonate/partner/{partnerUno}")
    @Operation(summary = "파트너 페이지 접근 토큰 생성", description = "관리자가 파트너 페이지에 접근할 수 있도록 토큰을 생성합니다.")
    public ResponseEntity<?> impersonatePartner(@PathVariable Long partnerUno) {
        try {
            // 현재 로그인한 관리자 정보 가져오기 (토큰에서)
            String token = jwtUtil.resolveToken(request);
            if (token == null || !jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("유효하지 않은 토큰입니다.");
            }

            // 관리자 권한 확인
            if (!jwtUtil.isAdmin(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("관리자 권한이 필요합니다.");
            }

            // 파트너 존재 여부 확인
            User partnerUser = userRepository.findByUno(partnerUno)
                    .orElse(null);
            if (partnerUser == null || partnerUser.getPartner() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("파트너를 찾을 수 없습니다.");
            }

            // 파트너 권한으로 임시 토큰 생성
            String impersonatedToken = jwtUtil.generatePartnerImpersonationToken(
                    partnerUser.getUid(),
                    partnerUser.getUname(),
                    "PARTNER",
                    partnerUno,
                    jwtUtil.getUnoFromToken(token) // 원본 관리자 uno 저장
            );

            Map<String, Object> response = new HashMap<>();
            response.put("impersonatedToken", impersonatedToken);
            response.put("partnerUno", partnerUno);
            response.put("partnerName", partnerUser.getPartner().getPname());
            response.put("originalAdminUno", jwtUtil.getUnoFromToken(token));

            log.info("관리자 {}가 파트너 {} 페이지 접근 토큰 생성", 
                    jwtUtil.getUnoFromToken(token), partnerUno);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("파트너 위임 토큰 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("토큰 생성 실패: " + e.getMessage());
        }
    }
}
