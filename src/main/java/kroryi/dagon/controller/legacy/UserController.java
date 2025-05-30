package kroryi.dagon.controller.legacy;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
//
//    @PostMapping("/api/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
//        if(authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            Claims claims = Jwts.parser()
//        }
//
//    }

    @GetMapping("/find-id")
    public String findId(){
        return "find-id";
    }

    @GetMapping("/find-password")
    public String findPassword(){
        return "find-password";
    }


}

