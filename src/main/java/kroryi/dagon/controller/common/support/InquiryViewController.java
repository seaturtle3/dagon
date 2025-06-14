package kroryi.dagon.controller.common.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryViewController {

    @GetMapping("")
    public String inquiry() {
        return "question/inquiry";
    }
}
