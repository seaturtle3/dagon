package kroryi.dagon.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.auth.RegisterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/users") // 변경: 끝에 '/' 제거
// J : ⚠ 실제 경로는 users 지만, 인증 관련 기능으로 auth 폴더에 위치시킴
@Tag(name = "Auth", description = "회원가입 API")
public class ApiAuthRegisterController {

    @Autowired
    private RegisterService registerService;


    @PostMapping("/register")
    @Operation(summary = "회원 등록 ", description = "회원 등록")
    public ResponseEntity<String> registerUser(@ModelAttribute UsersDTO usersDTO) {
        try {
            // 전화번호 합치기
            log.info("phone1: {}, phone2: {}, phone3: {}", usersDTO.getPhone1(), usersDTO.getPhone2(), usersDTO.getPhone3());
            usersDTO.setPhone();
            log.info("합쳐진 fullPhone: {}", usersDTO.getFullPhone());

            registerService.register(usersDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
}

