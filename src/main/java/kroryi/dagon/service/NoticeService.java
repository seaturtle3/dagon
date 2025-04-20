package kroryi.dagon.service;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.NoticeRepository;

import kroryi.dagon.util.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    public Notice updateNotice(Long id, NoticeRequestDTO dto) {
        Notice notice = noticeRepository.findById(id).orElseThrow();

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setModifyAt(java.time.LocalDateTime.now());

        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();

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

}
