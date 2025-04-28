package kroryi.dagon.controller.api.etc;

import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.ImageCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Tag(name = "5-4. Image(Admin)", description = "이미지 정리 관리자 기능")

@RequestMapping("/api/admin/image")
@RequiredArgsConstructor
public class ImageTestController {

    private final ImageCleanupService imageCleanupService;

    @PostMapping("/cleanup")
    public void cleanup() throws IOException {
        imageCleanupService.cleanUnusedImages();
    }
}