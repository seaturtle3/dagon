package kroryi.dagon.controller;


import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "user/login";
    }
}

