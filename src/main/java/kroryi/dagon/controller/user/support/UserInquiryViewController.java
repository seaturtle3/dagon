package kroryi.dagon.controller.user.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class UserInquiryViewController {

    @GetMapping("")
    public String inquiry() {
        return "question/inquiry";
    }
}
