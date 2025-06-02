package kroryi.dagon.service.support;

import jakarta.persistence.EntityNotFoundException;

import kroryi.dagon.DTO.InquiryCreateRequestDTO;
import kroryi.dagon.DTO.InquiryResponseDTO;
import kroryi.dagon.DTO.InquiryUpdateRequestDTO;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.repository.NotificationRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository; // 답변자(관리자 or 시스템) 찾기용
    private final PartnerRepository partnerRepository;

    // 1. 문의 생성
    @Transactional
    public InquiryResponseDTO createInquiry(Long userId, InquiryCreateRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Inquiry.InquiryBuilder inquiryBuilder = Inquiry.builder()
                .user(user)
                .inquiryType(request.getInquiryType())
                .title(request.getTitle())
                .content(request.getContent())
                .receiverType(request.getReceiverType());

        if (request.getReceiverType() == ReceiverType.PARTNER) {
            Partner partner = partnerRepository.findByPname(request.getPartnerName())
                    .orElseThrow(() -> new EntityNotFoundException("Partner (업체명) not found"));
            inquiryBuilder.partner(partner);
        }

        Inquiry savedInquiry = inquiryRepository.save(inquiryBuilder.build());

        return toResponseDTO(savedInquiry);
    }

    // 2. 문의 리스트 조회 (검색 + 페이징)
    public Page<InquiryResponseDTO> getAdminInquiries(Pageable pageable, String keyword) {
        Page<Inquiry> page;
        if (keyword == null || keyword.isBlank()) {
            page = inquiryRepository.findByReceiverType(ReceiverType.ADMIN, pageable);
        } else {
            page = inquiryRepository.findByReceiverTypeAndKeyword(ReceiverType.ADMIN, keyword, pageable);
        }

        return page.map(this::toResponseDTO);
    }

    // 3. 문의 단건 조회
    public InquiryResponseDTO getInquiry(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found"));
        return toResponseDTO(inquiry);
    }

    // 4. 문의 수정 (작성자만 가능)
    @Transactional
    public InquiryResponseDTO updateInquiry(Long userId, Long inquiryId, InquiryUpdateRequestDTO request) throws AccessDeniedException {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found with id: " + inquiryId));

        if (!inquiry.getUser().getUno().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this inquiry");
        }

        inquiry.setInquiryType(request.getInquiryType());
        inquiry.setTitle(request.getTitle());
        inquiry.setContent(request.getContent());

        return toResponseDTO(inquiry);
    }

    // 관리자용: 문의 ID만 있으면 삭제
    @Transactional
    public boolean deleteInquiryByAdmin(Long inquiryId) {
        if (!inquiryRepository.existsById(inquiryId)) {
            return false;
        }
        inquiryRepository.deleteById(inquiryId);
        return true;
    }

    // 사용자용: 문의 ID와 사용자 uno가 일치하는지 확인 후 삭제
    @Transactional
    public boolean deleteInquiryByUser(Long inquiryId, Long uno) {
        // 문의가 존재하고 uno가 맞는지 확인
        Optional<Inquiry> inquiryOpt = inquiryRepository.findById(inquiryId);
        if (inquiryOpt.isEmpty()) {
            return false;
        }
        Inquiry inquiry = inquiryOpt.get();

        // 문의 작성자 uno와 요청자 uno가 일치하는지 체크
        if (!inquiry.getUser().getUno().equals(uno)) {
            return false;
        }

        inquiryRepository.delete(inquiry);
        return true;
    }


    public List<Inquiry> getUserToPartnerInquiries(Long userUno, Long partnerUno) {
        return inquiryRepository.findByUser_UnoAndPartner_Uno(userUno, partnerUno);
    }

    public List<InquiryResponseDTO> getInquiriesToPartner(Long partnerUno) {
        List<Inquiry> inquiries = inquiryRepository.findByPartner_Uno(partnerUno);
        return inquiries.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }



private InquiryResponseDTO toResponseDTO(Inquiry inquiry) {
    return InquiryResponseDTO.builder()
            .id(inquiry.getId())
            .title(inquiry.getTitle())
            .content(inquiry.getContent())
            .inquiryType(inquiry.getInquiryType())
            .createdAt(inquiry.getCreatedAt())
            .updatedAt(inquiry.getUpdatedAt())
            .userName(inquiry.getUser().getUname())
            .userUid(inquiry.getUser().getUid())
            .partnerName(inquiry.getPartner() != null ? inquiry.getPartner().getPname() : null)
            .answeredAt(inquiry.getAnsweredAt())
            .answerContent(inquiry.getAnswerContent()) // 여기 추가 (필드 이름 맞게)
            .build();
}


}
