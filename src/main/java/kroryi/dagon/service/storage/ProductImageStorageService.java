package kroryi.dagon.service.storage;

import kroryi.dagon.service.image.FileStorageService;
import kroryi.dagon.entity.product.ProductImage;
import kroryi.dagon.repository.product.ProductImageRepository;
import kroryi.dagon.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ProductImageStorageService {

    private final Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "products");
    private final FileStorageUtil fileStorageUtil;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ProductImageStorageService(FileStorageUtil fileStorageUtil, ProductImageRepository productImageRepository) {
        this.fileStorageUtil = fileStorageUtil;
        this.productImageRepository = productImageRepository;
    }

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

    // 파일 저장 + DB 바이너리 저장
    public ProductImage saveWithDb(MultipartFile file, String folderName) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }
        try {
            // 파일 저장 (URL 반환)
            String savedUrl = fileStorageUtil.saveImage(file, folderName);
            // 파일 바이너리 추출
            byte[] imageBytes = file.getBytes();
            // DB 저장
            ProductImage image = new ProductImage();
            image.setFileName(savedUrl);
            image.setImageData(imageBytes);
            productImageRepository.save(image);
            return image;
        } catch (Exception e) {
            throw new RuntimeException("상품 이미지 저장(DB) 실패", e);
        }
    }
}