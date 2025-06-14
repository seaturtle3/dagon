package kroryi.dagon.controller.base.multtae;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MulttaeViewController {

    @GetMapping("/multtae")
    public String showMulttaePage() {
        return "board/multtae";
    }



}
