package kroryi.dagon.service.image;

import org.springframework.web.multipart.MultipartFile;
import kroryi.dagon.controller.common.image.ImageResponseDTO;

public interface ImageService {
    Long saveToDatabase(byte[] data, String originalFilename, String contentType, Long reportId);
    ImageResponseDTO loadFromDatabase(Long id);
} 