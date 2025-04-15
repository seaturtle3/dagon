package kroryi.dagon.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지", description = "로그인 폼을 반환합니다.")
    public String loginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 시도", description = "아이디와 비밀번호로 로그인 처리합니다.")
    public String loginInfo(@RequestParam String uid,
                            @RequestParam String upw,
                            HttpSession session) {
        System.out.println(uid + " / " + upw);

        // 로그인 검증
        UsersDTO user = userService.login(uid, upw);

        if (user != null) {
            System.out.println("Login Successful: " + user.getUname());

            // 세션에 유저 정보 저장
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

    @GetMapping("/logout/{id}")
    @Operation(summary = "로그아웃", description = "세션을 무효화하고 로그인 페이지로 이동합니다.")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "user/login";
    }
}

