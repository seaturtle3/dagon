package kroryi.dagon.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public abstract class FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public String store(MultipartFile file) {
        // 날짜별 디렉토리 생성 (YYYY/MM/DD 형식)
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        
        // 날짜별 디렉토리 경로 생성
        Path datePath = Paths.get(uploadDir, dateFolder);
        Path savePath = datePath.resolve(fileName);

        try {
            Files.createDirectories(datePath); // 날짜별 디렉토리 생성
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        // 브라우저에서 접근할 경로 반환 (날짜별 디렉토리 포함)
        return "/uploads/" + dateFolder + "/" + fileName;
    }

    public abstract String save(MultipartFile file);
}