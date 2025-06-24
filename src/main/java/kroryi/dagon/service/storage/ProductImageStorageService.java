package kroryi.dagon.service.storage;

import kroryi.dagon.service.image.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ProductImageStorageService {

    private final Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "products");

    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        try {
            // 디렉토리 없으면 생성
            Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(newFilename);

            file.transferTo(filePath.toFile());

            // 브라우저 접근용 경로 반환
            return "/uploads/products/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("상품 이미지 저장 실패", e);
        }
    }
}