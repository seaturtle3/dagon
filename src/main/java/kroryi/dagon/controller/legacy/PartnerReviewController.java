package kroryi.dagon.controller.legacy;

import kroryi.dagon.service.approval.PartnerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerReviewController {

    private final PartnerApplicationService applicationService;

    @GetMapping("/review")
    public String showReviewPage() {
        // "partner-review.html"을 반환하여 실제 HTML 페이지를 렌더링
        return "review"; // 반환할 HTML 파일 이름 (templates/partner-review.html)
    }


    @PostMapping("/approve/{id}")
    public ResponseEntity<Void> approve(@PathVariable Long id) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");

        return ResponseEntity.ok().build();
    }
}
