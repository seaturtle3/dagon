package kroryi.dagon.controller;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.UserService;
import kroryi.dagon.service.registerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class registerController {

    @Autowired
    private registerService registerService;

    @GetMapping("/register")
    public String registrationPage() {
        return "registration";  // registration.html 파일을 반환
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute UsersDTO usersDTO) {
        try {
            String fullPhone = usersDTO.getFullPhone();
            usersDTO.setFullPhone(fullPhone);


            registerService.register(usersDTO);
            return "/login";
        } catch (Exception e) {
            return "회원가입 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
