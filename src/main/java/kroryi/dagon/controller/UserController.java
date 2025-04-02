package kroryi.dagon.controller;

import kroryi.dagon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginFrom() {
        return "login";
    }

    @PostMapping("/login")
    public String loginInfo(@RequestParam String uid,
                            @RequestParam String upw) {
        System.out.println(uid + " / " + upw);

        boolean isLogin = userService.login(uid, upw);  // 서비스에서 로그인 검증

        if (isLogin) {
            System.out.println("Login Successful");
            return "index";
        } else {
            System.out.println("Login Failed");
            return "login";
        }

    }
}

