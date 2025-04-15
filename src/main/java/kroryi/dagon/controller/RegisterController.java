package kroryi.dagon.controller;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("/register")
    @Operation(summary = "회원가입 페이지", description = "회원가입 HTML 폼을 반환합니다.")
    public String registrationPage() {
        return "user/registration";  // registration.html 파일을 반환
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입 처리", description = "사용자 정보를 받아 회원가입을 처리합니다.")
    public String registerUser(@ModelAttribute UsersDTO usersDTO) {
        try {
            String fullPhone = usersDTO.getFullPhone();
            usersDTO.setFullPhone(fullPhone);

            registerService.register(usersDTO);
            return "user/login";
        } catch (Exception e) {
            // 예외 발생 시에도 뷰를 반환하고 있음 (이건 개선 가능)
            return "회원가입 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
