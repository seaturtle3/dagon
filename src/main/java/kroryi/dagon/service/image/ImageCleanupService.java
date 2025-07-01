package kroryi.dagon.service.image;

import jakarta.transaction.Transactional;
import kroryi.dagon.repository.board.NoticeRepository;
import kroryi.dagon.service.image.ImageContentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImageCleanupService {

    private final List<ImageContentProvider> contentProviders;

    @Value("${app.board.file.upload-dir}")
    private String baseDir;

    private static final Pattern IMG_SRC_PATTERN = Pattern.compile("<img[^>]*src=[\"'](/images/[^\"']+|/uploads/[^\"']+)[\"']");

    // @Scheduled(cron = "0 0 2 * * *") // 새벽 2시 실행 - 안전을 위해 비활성화
    public void cleanUnusedImages() throws IOException {
        log.info("이미지 정리 시작");

        Set<String> usedPaths = contentProviders.stream()
                .flatMap(provider -> provider.getAllContents().stream())
                .flatMap(content -> extractImagePaths(content).stream())
                .collect(Collectors.toSet());

        log.info("사용 중인 이미지 경로 수: {}", usedPaths.size());
        log.info("사용 중인 이미지 경로들: {}", usedPaths);

        if (usedPaths.size() < 10) {
            log.warn("사용 중인 이미지가 너무 적습니다 ({}개). 정리를 중단합니다.", usedPaths.size());
            return;
        }

        final int[] deletedCount = {0};
        final int[] totalFiles = {0};

        try (Stream<Path> files = Files.walk(Paths.get(baseDir))) {
            files.filter(Files::isRegularFile).forEach(file -> {
                totalFiles[0]++;
                String filePath = file.toString().replace("\\", "/");
                
                String imageUrl = convertFilePathToUrl(filePath);
                
                log.debug("검사 중인 파일: {} -> URL: {}", filePath, imageUrl);

                if (!usedPaths.contains(imageUrl)) {
                    try {
                        long fileSize = Files.size(file);
                        if (fileSize > 10 * 1024 * 1024) { // 10MB 이상
                            log.warn("파일이 너무 큽니다. 삭제하지 않습니다: {} (크기: {} bytes)", imageUrl, fileSize);
                            return;
                        }
                        
                        Files.delete(file);
                        deletedCount[0]++;
                        log.info("삭제됨: {} (원본 경로: {})", imageUrl, filePath);
                    } catch (IOException e) {
                        log.warn("삭제 실패: {} (원본 경로: {})", imageUrl, filePath, e);
                    }
                } else {
                    log.debug("사용 중인 이미지 유지: {}", imageUrl);
                }
            });
        }

        log.info("이미지 정리 완료 - 총 파일: {}, 삭제된 파일: {}", totalFiles[0], deletedCount[0]);
    }

    private String convertFilePathToUrl(String filePath) {
        String relativePath = filePath.replace(baseDir.replace("\\", "/"), "");
        
        if (relativePath.startsWith("/images/")) {
            return relativePath;
        }
        
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        
        if (!relativePath.startsWith("/uploads/") && !relativePath.startsWith("/images/")) {
            relativePath = "/uploads" + relativePath;
        }
        
        return relativePath;
    }

    private Set<String> extractImagePaths(String html) {
        Matcher matcher = IMG_SRC_PATTERN.matcher(html == null ? "" : html);
        return matcher.results()
                .map(m -> m.group(1))
                .collect(Collectors.toSet());
    }
}
