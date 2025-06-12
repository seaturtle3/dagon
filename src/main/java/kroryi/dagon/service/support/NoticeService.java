package kroryi.dagon.service.support;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.NoticeRepository;

import kroryi.dagon.util.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;
    private final ImageFileUtil imageFileUtil;

    public Page<Notice> findAllPaged(Pageable pageable) {
        return noticeRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable);
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }


    @Transactional
    public Notice createNotice(NoticeRequestDTO dto, String aid) {
        Admin admin = adminRepository.findById(aid).orElseThrow();

        Notice notice = new Notice();
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setAdmin(admin);

        return noticeRepository.save(notice);
    }

    @Transactional
    public Notice updateNotice(Long id, NoticeRequestDTO dto, String aid) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setModifyAt(java.time.LocalDateTime.now());
        notice.setAdmin(admin);

        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id, String aid) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        log.info("관리자 {} 가 공지사항 {} 삭제함", admin.getAname(), id);

        Set<String> imagesToCheck = imageFileUtil.extractImagePaths(notice.getContent());
        noticeRepository.delete(notice);

        List<String> otherUsedImages = noticeRepository.findAll().stream()
                .flatMap(n -> imageFileUtil.extractImagePaths(n.getContent()).stream())
                .toList();

        for (String img : imagesToCheck) {
            if (!otherUsedImages.contains(img)) {
                imageFileUtil.deleteImageFromDisk(img);
            }
        }
    }

    @Transactional
    public void increaseViews(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setViews(notice.getViews() + 1);
    }

    public Page<Notice> searchNotices(BoardSearchDTO dto, Pageable pageable) {
        String keyword = dto.getKeyword();
        String type = dto.getType();

        if (keyword == null || keyword.isBlank()) {
            return noticeRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable);
        }

        String keywordPattern = "%" + keyword.trim() + "%";

        System.out.println("Search Type: " + type);
        System.out.println("Search KeywordPattern: " + keywordPattern);

        return noticeRepository.searchByKeyword(keywordPattern, type, pageable);
    }


}
