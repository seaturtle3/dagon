package kroryi.dagon.service.image;

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

    private static final Pattern IMG_SRC_PATTERN = Pattern.compile("<img[^>]*src=[\"'](/images/[^\"']+)[\"']");

    @Scheduled(cron = "0 0 2 * * *") // 새벽 2시 실행
    public void cleanUnusedImages() throws IOException {
        log.info("이미지 정리 시작");

        Set<String> usedPaths = contentProviders.stream()
                .flatMap(provider -> provider.getAllContents().stream())
                .flatMap(content -> extractImagePaths(content).stream())
                .collect(Collectors.toSet());

        try (Stream<Path> files = Files.walk(Paths.get(baseDir))) {
            files.filter(Files::isRegularFile).forEach(file -> {
                String filePath = file.toString().replace("\\", "/");
                String imageUrl = filePath.replace(baseDir.replace("\\", "/"), "/images");

                if (!usedPaths.contains(imageUrl)) {
                    try {
                        Files.delete(file);
                        log.info("삭제됨: {}", imageUrl);
                    } catch (IOException e) {
                        log.warn("삭제 실패: {}", imageUrl, e);
                    }
                }
            });
        }

        log.info("이미지 정리 완료");
    }

    private Set<String> extractImagePaths(String html) {
        Matcher matcher = IMG_SRC_PATTERN.matcher(html == null ? "" : html);
        return matcher.results()
                .map(m -> m.group(1))
                .collect(Collectors.toSet());
    }
}
