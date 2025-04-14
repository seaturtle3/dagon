package kroryi.dagon.config;

import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.UsersDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// 모든 페이지 로그인 정보 저장 로직


@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("user")
    public UsersDTO addUserToModel(HttpSession session) {
        return (UsersDTO) session.getAttribute("loginUser");
    }
}