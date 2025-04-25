package kroryi.dagon.controller.api.etc;

import kroryi.dagon.service.ImageCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/image")
@RequiredArgsConstructor
public class ImageTestController {

    private final ImageCleanupService imageCleanupService;

    @PostMapping("/cleanup")
    public void cleanup() throws IOException {
        imageCleanupService.cleanUnusedImages();
    }
}