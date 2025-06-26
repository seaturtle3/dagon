package kroryi.dagon.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Component
public class FileStorageUtil {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public String saveImage(MultipartFile file, String folderName) {
        try {
            // 날짜별 디렉토리 생성 (YYYY/MM/DD 형식)
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            
            // 고유 파일명 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 저장 경로를 프로퍼티에서 읽어온 값으로 조합
            Path uploadPath = Paths.get(uploadDir, folderName, dateFolder);
            Files.createDirectories(uploadPath);

            // 실제 파일 저장
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // ✅ 클라이언트에서 접근할 수 있는 URL 경로로 변경 (날짜별 디렉토리 포함)
            return "/uploads/" + folderName + "/" + dateFolder + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    /**
     * 이미지를 저장하고, 같은 경로에 썸네일도 thumb_{원본파일명}으로 생성합니다.
     * 썸네일 크기는 400x300 고정입니다.
     * @param file 업로드 파일
     * @param folderName 저장 폴더명
     * @return 원본 이미지의 URL (필요시 썸네일 URL은 thumb_ 접두어로 조합)
     */
    public String saveImageWithThumbnail(MultipartFile file, String folderName) {
        try {
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir, folderName, dateFolder);
            Files.createDirectories(uploadPath);

            // 원본 저장
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 썸네일 생성 및 저장
            String thumbFileName = "thumb_" + fileName;
            Path thumbPath = uploadPath.resolve(thumbFileName);

            BufferedImage originalImage = ImageIO.read(filePath.toFile());
            Thumbnails.of(originalImage)
                .size(400, 300)
                .toFile(thumbPath.toFile());

            // 원본 이미지 URL 반환 (썸네일은 thumb_ 접두어로 조합)
            return "uploads/" + folderName + "/" + dateFolder + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장/썸네일 생성 실패", e);
        }
    }

    public String getUploadDir() {
        return uploadDir;
    }
}
