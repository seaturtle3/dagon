package kroryi.dagon.controller;

import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/partners")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @PostMapping("/review")
    public ResponseEntity<String> submitPartnerReview(@RequestBody PartnerDTO partnerDTO) throws Exception {
        System.out.println("Received data: " + partnerDTO);

        partnerService.partner(partnerDTO);
        return ResponseEntity.ok("신청이 완료되었습니다.");
    }

    @GetMapping("/review")
    public String getReviewPage() {
        return "review";
    }
}

