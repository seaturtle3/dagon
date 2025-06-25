package kroryi.dagon.controller.common.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import kroryi.dagon.service.image.ImageService;
import kroryi.dagon.controller.common.image.ImageResponseDTO;

@Tag(name = "Image", description = "이미지 업로드 및 조회 API")
@RestController
@Log4j2
@RequestMapping("/api/images")
public class ImageController {

    @Value("${app.board.file.upload-dir}")
    private String baseUploadDir;

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "이미지 업로드", description = "이미지 업로드 API")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@Parameter(description = "업로드할 이미지 파일", required = true)
                                              @RequestPart("image") MultipartFile file,
                                              @RequestParam("reportId") Long reportId) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("빈파일");
        }

        try {
            byte[] data = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            // DB에 저장, id 반환
            Long imageId = imageService.saveToDatabase(data, originalFilename, contentType, reportId);
            return ResponseEntity.ok(imageId.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageResponseDTO imageData = imageService.loadFromDatabase(id); // ImageResponseDTO: byte[], contentType 포함 객체
        if (imageData == null || imageData.getData() == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageData.getContentType()));
        return new ResponseEntity<>(imageData.getData(), headers, HttpStatus.OK);
    }

}
