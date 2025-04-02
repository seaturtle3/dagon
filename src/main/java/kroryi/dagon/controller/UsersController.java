package kroryi.dagon.controller;


import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.Service.UsersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.transform.sax.SAXResult;

@Log4j2
@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/signup")
    public String signup(UsersDTO dto, Model model) {
        boolean isSuccess = usersService.registerUser(dto);

        if(isSuccess) {
            model.addAttribute("message", "회원가입이 성공했습니다.");
            return "index.html";
        }else {
            model.addAttribute("message", "아이디가 중복되었습니다.");
            return "register.html";
        }


    }
    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

}
