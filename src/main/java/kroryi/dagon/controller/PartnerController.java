package kroryi.dagon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.compoent.CustomUserDetails;

import kroryi.dagon.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


//            파트너 등록 컨트롤

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/review")
    public ResponseEntity<String> submitPartnerReview(@RequestBody PartnerApplicationDTO partnerApplicationDTO,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        partnerApplicationDTO.setUno(userDetails.getUno());
        partnerApplicationDTO.setUname(userDetails.getUsername());

        System.out.println("받은 요청 DTO: " + partnerApplicationDTO);

        partnerService.partner(partnerApplicationDTO);

        return ResponseEntity.ok("신청이 완료되었습니다!");
    }

    @GetMapping("/review")
    public String getReviewPage() {
        return "review";
    }
}


