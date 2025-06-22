package kroryi.dagon.service.storage;

import kroryi.dagon.service.image.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class LocalFileStorageService extends FileStorageService {

    @Override
    public String save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        try {
            // 날짜별 디렉토리 생성 (YYYY/MM/DD 형식)
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID() + extension;
            // 실제 경로로 바꿔줘 (날짜별 디렉토리 포함)
            String uploadDir = "D:/project/dagon/uploads/fishing-report/";
            Path datePath = Paths.get(uploadDir, dateFolder);
            Path filePath = datePath.resolve(newFilename);

            // 날짜별 디렉토리 생성
            Files.createDirectories(datePath);
            file.transferTo(filePath.toFile());

            // 반환할 URL 경로 (날짜별 디렉토리 포함)
            return "/uploads/fishing-report/" + dateFolder + "/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
