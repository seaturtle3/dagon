package kroryi.dagon.controller.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.RegisterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/users") // 변경: 끝에 '/' 제거
@Tag(name = "User", description = "회원가입, 로그인, 마이페이지, 계정 관리 API")
public class ApiRegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/register")
    @Operation(summary = "회원 등록 ", description = "회원 등록")
    public ResponseEntity<String> registerUser(@ModelAttribute UsersDTO usersDTO) {
        try {
            registerService.register(usersDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
}

