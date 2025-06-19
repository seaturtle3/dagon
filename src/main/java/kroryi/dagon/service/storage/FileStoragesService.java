package kroryi.dagon.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragesService {
    String save(MultipartFile file);
}
