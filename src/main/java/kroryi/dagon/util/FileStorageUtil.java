package kroryi.dagon.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Component
public class FileStorageUtil {

    public String saveImage(MultipartFile file, String folderName) {
        try {
            // 고유 파일명 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 저장 경로 구성
            Path uploadPath = Paths.get("uploads/" + folderName);
            Files.createDirectories(uploadPath);

            // 실제 파일 저장
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // ✅ 클라이언트에서 접근할 수 있는 URL 경로로 변경
            return "/uploads/" + folderName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}
