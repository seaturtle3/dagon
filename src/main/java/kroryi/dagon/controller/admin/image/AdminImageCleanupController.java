package kroryi.dagon.controller.admin.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.image.ImageCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Image", description = "이미지 관리 API (관리자)")
@RequestMapping("/api/admin/image")
@RequiredArgsConstructor
public class AdminImageCleanupController {

    private final ImageCleanupService imageCleanupService;

    @PostMapping("/cleanup")
    @Operation(summary = "사용하지 않는 이미지 정리", description = "DB에서 참조되지 않는 이미지 파일들을 삭제합니다.")
    public ResponseEntity<Map<String, Object>> cleanup() throws IOException {
        Map<String, Object> response = new HashMap<>();
        
        try {
            imageCleanupService.cleanUnusedImages();
            response.put("success", true);
            response.put("message", "이미지 정리가 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "이미지 정리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}