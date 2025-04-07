package kroryi.dagon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.DTO.UsersDTO;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Controller
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/review")
    public ResponseEntity<String> submitPartnerReview(@RequestBody PartnerApplicationDTO partnerApplicationDTO,
                                                      HttpSession session) throws Exception {
        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다."); // 미 로그인시 로그인하라는 표시
        }

        partnerApplicationDTO.setUno(loginUser.getUno());
        partnerApplicationDTO.setUname(loginUser.getUname()); // 로그인 정보 가져오는 로직

        System.out.println("받은 요청 DTO: " + partnerApplicationDTO);

        partnerService.partner(partnerApplicationDTO);

        return ResponseEntity.ok("신청이 완료되었습니다!.");

    }

    @GetMapping("/review")
    public String getReviewPage() {
        return "review";
    }
}

