package kroryi.dagon.controller.legacy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerApplicationController {

    /**
     * 파트너 신청 목록 페이지 (템플릿 반환)
     */
    @GetMapping("/list")
    public String listPartnerApplications() {
        return "partner/list";
    }

    /**
     * 파트너 신청 상세 페이지 (템플릿 반환)
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id) {
        return "partner/detail";
    }

    @GetMapping("/my_page")
    public String my_page () {

        return "/partner/partner-my-page";
    }
}