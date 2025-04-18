package kroryi.dagon.service;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.repository.board.AdminRepository;
import kroryi.dagon.repository.board.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    public List<Notice> findAll(){
        return noticeRepository.findAllByOrderByIsTopDescCreatedAtDesc();
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
        notice.setThumbnailUrl(dto.getThumbnailUrl());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setAdmin(admin);
        return noticeRepository.save(notice);
    }

    @Transactional
    public Notice updateNotice(Long id, NoticeRequestDTO dto){
        Notice notice = noticeRepository.findById(id).orElseThrow();

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setThumbnailUrl(dto.getThumbnailUrl());
        notice.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        notice.setModifyAt(java.time.LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    @Transactional
    public void deleteNotice(Long id){
        noticeRepository.deleteById(id);
    }

    @Transactional
    public void increaseViews(Long id){
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setViews(notice.getViews() + 1);
    }
}
