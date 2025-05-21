package kroryi.dagon.controller.admin.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.ImageCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Tag(name = "Image", description = "이미지 관리 API (관리자)")
@RequestMapping("/api/admin/image")
@RequiredArgsConstructor
public class AdminImageCleanupController {

    private final ImageCleanupService imageCleanupService;

    @PostMapping("/cleanup")
    public void cleanup() throws IOException {
        imageCleanupService.cleanUnusedImages();
    }
}