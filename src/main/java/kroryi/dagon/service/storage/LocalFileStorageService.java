package kroryi.dagon.service.storage;

import kroryi.dagon.service.image.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService extends FileStorageService {

    @Override
    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID() + extension;
            // 실제 경로로 바꿔줘
            String uploadDir = "D:/project/dagon/uploads/fishing-report/";
            Path filePath = Paths.get(uploadDir, newFilename);

            // 디렉토리 없으면 생성
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath.toFile());

            // 반환할 URL 경로 (예: 서버에서 /uploads/...로 접근 가능)
            return "/uploads/fishing-report/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
