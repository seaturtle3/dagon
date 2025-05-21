package kroryi.dagon.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.UUID;
@Tag(name = "Image", description = "이미지 업로드 및 조회 API")
@RestController
@Log4j2
public class ImageController {

    @Value("${app.board.file.upload-dir}")
    private String baseUploadDir;

    @Operation(summary = "이미지 업로드", description = "이미지 업로드 API")
    @PostMapping(value = "/uploadImage", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@Parameter(description = "업로드할 이미지 파일", required = true)
                                              @RequestPart("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("빈파일");
        }

        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path uploadPath = Paths.get(baseUploadDir).resolve(dateFolder);

        try {
            Files.createDirectories(uploadPath);

            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String storedFileName = UUID.randomUUID() + "." + ext;
            Path targetPath = uploadPath.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String url = "/images/" + dateFolder + "/" + storedFileName;
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패");
        }

    }

    @Operation(summary = "이미지 조회", description = "저장된 이미지 파일을 URL 경로를 통해 조회\n" +
            "예: /images/2025/04/21/uuid-filename.png 형식의 경로로 접근")
    @GetMapping("/images/{year}/{month}/{day}/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String year,
                                               @PathVariable String month,
                                               @PathVariable String day,
                                               @PathVariable String filename) throws IOException {
        Path path = Paths.get(baseUploadDir, year, month, day, filename);
        log.info("pat--->: {}", path);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
