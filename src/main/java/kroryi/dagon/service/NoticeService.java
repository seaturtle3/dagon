package kroryi.dagon.service;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    public Page<Notice> findAllPaged(Pageable pageable){
        return noticeRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable);
    }
    public Notice findById(Long id){
        return noticeRepository.findById(id).orElse(null);
    }

    @Transactional
    public Notice createNotice(NoticeRequestDTO dto, String aid){
        Admin admin = adminRepository.findById(aid).orElseThrow();
        Notice notice = new Notice();

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
//        notice.setThumbnailUrl(dto.getThumbnailUrl());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setAdmin(admin);
        return noticeRepository.save(notice);
    }

    @Transactional
    public Notice updateNotice(Long id, NoticeRequestDTO dto){
        Notice notice = noticeRepository.findById(id).orElseThrow();

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
//        notice.setThumbnailUrl(dto.getThumbnailUrl());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setModifyAt(java.time.LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();

        // 1. 삭제 전 이미지 경로 추출
        Set<String> imagesToCheck = extractImagePaths(notice.getContent());

        // 2. 게시글 삭제
        noticeRepository.delete(notice);

        // 3. 다른 공지에서 동일 이미지 사용하는지 확인
        List<String> otherUsedImages = noticeRepository.findAll().stream()
                .flatMap(n -> extractImagePaths(n.getContent()).stream())
                .toList();

        // 4. 사용되지 않는 이미지 삭제
        for (String img : imagesToCheck) {
            if (!otherUsedImages.contains(img)) {
                deleteImageFromDisk(img);
            }
        }
    }

    @Transactional
    public void increaseViews(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setViews(notice.getViews() + 1);
    }

    // 이미지 src 추출
    private Set<String> extractImagePaths(String html) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"'](/images/[^\"']+)[\"']");
        Matcher matcher = pattern.matcher(html);
        return matcher.results()
                .map(m -> m.group(1)) // "/images/2025/04/18/파일명.png"
                .collect(Collectors.toSet());
    }

    // 이미지 실제 파일 삭제
    private void deleteImageFromDisk(String imageUrl) {
        try {
            // 예: "/images/2025/04/18/abc.png" → uploads/2025/04/18/abc.png
            String relativePath = imageUrl.replace("/images/", "").replace("/", File.separator);
            Path filePath = Paths.get("C:/Users/edu007/IdeaProjects/dagon/uploads").resolve(relativePath);

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
