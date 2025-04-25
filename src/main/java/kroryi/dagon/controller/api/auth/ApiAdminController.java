package kroryi.dagon.controller.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.AdminDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.AdminService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "1-2. Admin", description = "관리자 인증 및 사용자 관리 API")
public class ApiAdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


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
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody AdminDTO adminLoginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(adminLoginDTO.getAid(), adminLoginDTO.getApw())
            );

            // 인증이 성공하면 JWT 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(adminLoginDTO.getAid(), 0L, "admin", "admin");

            Map<String, String> response = new HashMap<>();
            response.put("token", token);  // JSON 응답 반환

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "잘못된 관리자 아이디나 비밀번호입니다."));
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

    // 회원 수정
    @PutMapping("/user/{uno}")
    @Operation(summary = "회원 수정 ", description = "회원 수정")
    public UsersDTO updateUser(@PathVariable String uno, @RequestBody UsersDTO dto) {
        return adminService.updateUser((uno), dto);
    }

    // 회원 삭제
    @DeleteMapping("/user/{uno}")
    public ResponseEntity<?> deleteUser(@PathVariable String uno) {
        try {
            adminService.deleteUser(uno);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("회원 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: " + e.getMessage());
        }
    }
}
