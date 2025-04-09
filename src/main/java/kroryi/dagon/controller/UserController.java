package kroryi.dagon.controller;


import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/login")
    public String loginInfo(@RequestParam String uid,
                            @RequestParam String upw,
                            HttpSession session) {
        System.out.println(uid + " / " + upw);

        // 로그인 검증
        UsersDTO user = userService.login(uid, upw);

        if (user != null) {
            System.out.println("Login Successful: " + user.getUname());

            // 세션에 유저 번호와 이름 저장
            session.setAttribute("uno", user.getUno());
            session.setAttribute("uname", user.getUname());
            session.setAttribute("uid", user.getUid());
            session.setAttribute("upw", user.getUpw());
            session.setAttribute("loginUser", user);
            session.setAttribute("displayName", user.getDisplayName());


            System.out.println("세션 확인: " + session.getAttribute("uname"));
            return "index";

        } else {
            System.out.println("Login Failed");

            return "user/login";

        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "user/login";
    }
}

