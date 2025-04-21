package kroryi.dagon.controller;


import kroryi.dagon.entity.User;
import kroryi.dagon.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@Log4j2
@AllArgsConstructor
public class MyPageController {

    private final UserService userService;  // 사용자 서비스

    @GetMapping
    public String myPage() {

        return "user/my_page";
    }
}
