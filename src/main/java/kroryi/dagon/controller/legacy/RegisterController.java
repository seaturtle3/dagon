package kroryi.dagon.controller.legacy;

import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.RegisterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequestMapping("/web/users") // 변경: "/web" 추가
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @GetMapping("register")
    public String registrationPage() {
        return "user/registration";  // registration.html 파일을 반환
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute UsersDTO usersDTO, Model model) {
        log.info("----------------->{}",usersDTO);

        try {
            usersDTO.setFullPhone(usersDTO.getFullPhone());
            registerService.register(usersDTO);
            return "redirect:/login"; // ✅ 성공 시 로그인 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/registration"; // 다시 회원가입 화면
        }
    }
}

