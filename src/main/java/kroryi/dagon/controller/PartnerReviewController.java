package kroryi.dagon.controller;

import kroryi.dagon.service.PartnerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerReviewController {

    private final PartnerApplicationService applicationService;

    @PostMapping("/approve/{id}")

    public ResponseEntity<Void> approve(@PathVariable Long id) {
        applicationService.approve(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        applicationService.reject(id, reason);
        return ResponseEntity.ok().build();
    }
}
