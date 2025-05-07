package kroryi.dagon.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Log4j2
@AllArgsConstructor
public class AdminController {

    // 관리자 로그인 페이지
    @GetMapping("/login")
    public String adminLogin() {
        return "admin/admin_login";
    }

    // 관리자 회원가입 페이지
    @GetMapping("/registration")
    public String adminRegistration() {
        return "admin/admin_registration";
    }

    // 관리자  페이지
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/admin_dashboard";
    }

    /**
     * API Key 생성
     */
    @GetMapping("/api-keys/new")
    public String newApiKey() {

        return "/admin/create-api-key";
    }

    @GetMapping("/api-keys/list")
    public String listApiKey() {

        return "/admin/list-api-keys";
    }


}

