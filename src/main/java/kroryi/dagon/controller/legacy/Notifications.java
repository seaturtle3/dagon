package kroryi.dagon.controller.legacy;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class Notifications {


    @GetMapping
    public String notifications() {

        return "notifications";
    }


    @GetMapping("/admin")
    public String admin() {

        return "createNotification";
    }
}
