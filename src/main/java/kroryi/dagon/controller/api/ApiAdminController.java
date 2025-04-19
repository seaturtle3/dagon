package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.AdminDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.AdminService;
import kroryi.dagon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ApiAdminController {

    private final AdminService adminService;


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


    // 전체 회원 조회 (페이징)
    @GetMapping
    @Operation(summary = "전체 회원 조회 ", description = "전체 회원 조회")
    public Page<UsersDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllUsers(PageRequest.of(page, size));
    }

    // 회원 상세 조회
    @GetMapping("/{uid}")
    @Operation(summary = "상세 조회 ", description = "상세 조회")
    public UsersDTO getUser(@PathVariable String uid) {
        return adminService.getUserByUid(uid);
    }

    // 회원 수정
    @PutMapping("/{uid}")
    @Operation(summary = "회원 수정 ", description = "회원 수정")
    public UsersDTO updateUser(@PathVariable String uid, @RequestBody UsersDTO dto) {
        return adminService.updateUser((uid), dto);
    }

    // 회원 삭제
    @DeleteMapping("/{uid}")
    @Operation(summary = "회원 삭제 ", description = "회원 삭제")
    public void deleteUser(@PathVariable String uid) {
        adminService.deleteUser(uid);
    }
}
