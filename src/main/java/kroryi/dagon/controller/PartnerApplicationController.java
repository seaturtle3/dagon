package kroryi.dagon.controller;

import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.service.PartnerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


// 파트너 신청 리스트 컨트롤

@Controller
@RequiredArgsConstructor
@RequestMapping("/partners") // ⚠️ URL 깔끔하게 정리
public class PartnerApplicationController {

    private final PartnerApplicationService partnerApplicationService;

    @GetMapping("/list") // 명시적으로 list URL로 바꿈
    public String listPartnerApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<PartnerApplicationDTO> applicationPage = partnerApplicationService.getAllApplications(PageRequest.of(page, size));
        model.addAttribute("applicationPage", applicationPage);
        return "partner/list"; // => templates/partner/list.html
    }

    @GetMapping("/detail/{id}")
    public String partnerDetail(@PathVariable Long id, Model model) {
        PartnerApplicationDTO app = partnerApplicationService.findById(id); // 서비스에 따로 구현
        model.addAttribute("app", app);
        return "partner/detail"; // => templates/partner/detail.html
    }
}