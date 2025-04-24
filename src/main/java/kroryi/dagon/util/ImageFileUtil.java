package kroryi.dagon.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class ImageFileUtil {

    @Value("${app.board.file.upload-dir}")
    private String uploadBasePath;

    public Set<String> extractImagePaths(String html) {
        if (html == null || html.isBlank()) return Set.of();

        Pattern pattern = Pattern.compile("<img[^>]*src=[\"'](/images/[^\"']+)[\"']");
        Matcher matcher = pattern.matcher(html);

        return matcher.results()
                .map(m -> m.group(1))
                .collect(Collectors.toSet());
    }

    public void deleteImageFromDisk(String imageUrl) {
        try {
            String relativePath = imageUrl.replace("/images/", "").replace("/", File.separator);
            Path filePath = Paths.get(uploadBasePath).resolve(relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("🗑️ 삭제된 이미지: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("❗ 이미지 삭제 실패: " + imageUrl);
            e.printStackTrace();
        }
    }

}
