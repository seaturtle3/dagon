package kroryi.dagon.service;

import jakarta.transaction.Transactional;
import kroryi.dagon.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    private final NoticeRepository noticeRepository;

    private static final String BASE_DIR = "C:/Users/edu007/IdeaProjects/dagon/uploads";
    private static final Pattern IMG_SRC_PATTERN = Pattern.compile("<img[^>]*src=[\"'](/images/[^\"']+)[\"']");

    /**
     * 주기적으로 실행
     */
    @Scheduled(cron = "0 0 2 * * *") //
    @Transactional
    public void cleanUnusedImages() throws IOException {
        log.info("이미지 정리 시작");

        // 1. 게시글에서 사용 중인 이미지 경로 수집
        List<String> usedPaths = noticeRepository.findAll().stream()
                .flatMap(n -> extractImagePaths(n.getContent()).stream())
                .collect(Collectors.toList());

        // 2. 실제 파일 시스템에서 모든 이미지 파일 찾기
        try (Stream<Path> files = Files.walk(Paths.get(BASE_DIR))) {
            files.filter(Files::isRegularFile).forEach(file -> {
                String relativePath = BASE_DIR.replace("\\", "/");
                String filePath = file.toString().replace("\\", "/");
                String imageUrl = filePath.replace(relativePath, "/images");

                // 3. 게시글에서 참조하지 않으면 삭제
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
        Matcher matcher = IMG_SRC_PATTERN.matcher(html);
        return matcher.results()
                .map(m -> m.group(1)) // /images/2025/04/18/abc.jpg
                .collect(Collectors.toSet());
    }
}
