package kroryi.dagon.controller;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.compoent.CustomUserDetails;
import kroryi.dagon.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//            파트너 등록 컨트롤
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/partner")
public class ApiPartnerController {

    private final PartnerService partnerService;

    @PostMapping("/review")
    public ResponseEntity<String> submitPartnerReview(
            @RequestBody PartnerApplicationDTO partnerApplicationDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        log.info("🔥 컨트롤러 진입 {}", partnerApplicationDTO);
        log.info("🔥 컨트롤러 진입1 {}", userDetails);
        if (userDetails == null) {
            log.warn("❌ 인증 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        log.info("✅ 인증된 유저: uno={}, uname={}", userDetails.getUno(), userDetails.getUsername());

        partnerApplicationDTO.setUno(userDetails.getUno());
        partnerApplicationDTO.setUname(userDetails.getUsername());

        partnerService.partner(partnerApplicationDTO);
        return ResponseEntity.ok("신청이 완료되었습니다!");
    }


}


