package kroryi.dagon.service.support;

import jakarta.persistence.EntityNotFoundException;

import kroryi.dagon.DTO.InquiryCreateRequestDTO;
import kroryi.dagon.DTO.InquiryResponseDTO;
import kroryi.dagon.DTO.InquiryUpdateRequestDTO;
import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.enums.WriterType;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.repository.NotificationRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository; // 답변자(관리자 or 시스템) 찾기용
    private final PartnerRepository partnerRepository;

    /**
     * 1:1 문의 생성
     * @param userId 사용자 ID
     * @param request 문의 생성 요청 DTO
     * @return 생성된 문의 응답 DTO
     */
    @Transactional
    public InquiryResponseDTO createInquiry(Long userId, InquiryCreateRequestDTO request) {
        log.info("문의 생성 시작 - 사용자 ID: {}, 제목: {}", userId, request.getTitle());
        
        try {
            // 사용자 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

            // 문의 빌더 생성
            Inquiry.InquiryBuilder inquiryBuilder = Inquiry.builder()
                    .user(user)
                    .inquiryType(request.getInquiryType())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now());

            // 파트너 문의인 경우
            if (request.getPartnerId() != null) {
                log.info("파트너 문의 생성 - 파트너 ID: {}", request.getPartnerId());
                
                Partner partner = partnerRepository.findById(request.getPartnerId())
                        .orElseThrow(() -> new EntityNotFoundException("파트너를 찾을 수 없습니다. ID: " + request.getPartnerId()));
                
                inquiryBuilder.partner(partner);
                inquiryBuilder.receiverType(ReceiverType.PARTNER);
                inquiryBuilder.writerType(WriterType.valueOf(request.getWriterType()));
                
            } else {
                // 관리자 문의인 경우
                inquiryBuilder.receiverType(ReceiverType.ADMIN);
                inquiryBuilder.writerType(WriterType.valueOf(request.getWriterType()));
            }

            // 문의 저장
            Inquiry savedInquiry = inquiryRepository.save(inquiryBuilder.build());
            log.info("문의 생성 완료 - 문의 ID: {}", savedInquiry.getId());
            
            return toResponseDTO(savedInquiry);
            
        } catch (Exception e) {
            log.error("문의 생성 중 오류 발생 - 사용자 ID: {}, 오류: {}", userId, e.getMessage(), e);
            throw new RuntimeException("문의 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 관리자용 문의 리스트 조회 (검색 + 페이징)
     * @param pageable 페이징 정보
     * @param keyword 검색 키워드
     * @param status 답변 상태 필터
     * @param inquiryType 문의 유형 필터
     * @return 문의 페이지
     */
    public Page<InquiryResponseDTO> getAdminInquiries(Pageable pageable, String keyword, Boolean status, String inquiryType) {
        log.info("관리자 문의 리스트 조회 - 페이지: {}, 키워드: {}, 상태: {}, 문의유형: {}", 
                pageable.getPageNumber(), keyword, status, inquiryType);
        
        try {
            Page<Inquiry> page;
            
            // inquiryType을 enum으로 변환
            kroryi.dagon.enums.InquiryType inquiryTypeEnum = null;
            if (inquiryType != null && !inquiryType.trim().isEmpty()) {
                try {
                    inquiryTypeEnum = kroryi.dagon.enums.InquiryType.valueOf(inquiryType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 inquiryType: {}", inquiryType);
                }
            }
            
            // 검색 조건에 따른 분기 처리
            if (keyword == null || keyword.isBlank()) {
                if (inquiryTypeEnum != null) {
                    if (status != null) {
                        page = inquiryRepository.findByReceiverTypeAndInquiryTypeAndIsAnswered(
                                ReceiverType.ADMIN, inquiryTypeEnum, status, pageable);
                    } else {
                        page = inquiryRepository.findByReceiverTypeAndInquiryType(
                                ReceiverType.ADMIN, inquiryTypeEnum, pageable);
                    }
                } else {
                    if (status != null) {
                        page = inquiryRepository.findByReceiverTypeAndIsAnswered(
                                ReceiverType.ADMIN, status, pageable);
                    } else {
                        page = inquiryRepository.findByReceiverType(ReceiverType.ADMIN, pageable);
                    }
                }
            } else {
                if (inquiryTypeEnum != null) {
                    if (status != null) {
                        page = inquiryRepository.findByReceiverTypeAndInquiryTypeAndIsAnsweredAndKeyword(
                                ReceiverType.ADMIN, inquiryTypeEnum, status, keyword, pageable);
                    } else {
                        page = inquiryRepository.findByReceiverTypeAndInquiryTypeAndKeyword(
                                ReceiverType.ADMIN, inquiryTypeEnum, keyword, pageable);
                    }
                } else {
                    if (status != null) {
                        page = inquiryRepository.findByReceiverTypeAndIsAnsweredAndKeyword(
                                ReceiverType.ADMIN, status, keyword, pageable);
                    } else {
                        page = inquiryRepository.findByReceiverTypeAndKeyword(
                                ReceiverType.ADMIN, keyword, pageable);
                    }
                }
            }

            Page<InquiryResponseDTO> result = page.map(this::toResponseDTO);
            log.info("관리자 문의 리스트 조회 완료 - 총 개수: {}", result.getTotalElements());
            
            return result;
            
        } catch (Exception e) {
            log.error("관리자 문의 리스트 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("문의 리스트 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 문의 단건 조회
     * @param id 문의 ID
     * @return 문의 응답 DTO
     */
    public InquiryResponseDTO getInquiry(Long id) {
        log.info("문의 단건 조회 - 문의 ID: {}", id);
        
        try {
            Inquiry inquiry = inquiryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("문의를 찾을 수 없습니다. ID: " + id));
            
            InquiryResponseDTO result = toResponseDTO(inquiry);
            log.info("문의 단건 조회 완료 - 문의 ID: {}", id);
            
            return result;
            
        } catch (Exception e) {
            log.error("문의 단건 조회 중 오류 발생 - 문의 ID: {}, 오류: {}", id, e.getMessage(), e);
            throw new RuntimeException("문의 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 문의 수정 (작성자만 가능)
     * @param userId 사용자 ID
     * @param inquiryId 문의 ID
     * @param request 수정 요청 DTO
     * @return 수정된 문의 응답 DTO
     * @throws AccessDeniedException 권한이 없는 경우
     */
    @Transactional
    public InquiryResponseDTO updateInquiry(Long userId, Long inquiryId, InquiryUpdateRequestDTO request) throws AccessDeniedException {
        log.info("문의 수정 시작 - 사용자 ID: {}, 문의 ID: {}", userId, inquiryId);
        
        try {
            Inquiry inquiry = inquiryRepository.findById(inquiryId)
                    .orElseThrow(() -> new EntityNotFoundException("문의를 찾을 수 없습니다. ID: " + inquiryId));

            // 권한 확인
            if (!inquiry.getUser().getUno().equals(userId)) {
                log.warn("문의 수정 권한 없음 - 사용자 ID: {}, 문의 작성자 ID: {}", userId, inquiry.getUser().getUno());
                throw new AccessDeniedException("해당 문의를 수정할 권한이 없습니다.");
            }

            // 답변이 있는 경우 수정 불가
            if (inquiry.isAnswered()) {
                log.warn("답변이 있는 문의는 수정할 수 없음 - 문의 ID: {}", inquiryId);
                throw new IllegalStateException("답변이 있는 문의는 수정할 수 없습니다.");
            }

            // 문의 수정
            inquiry.setInquiryType(request.getInquiryType());
            inquiry.setTitle(request.getTitle());
            inquiry.setContent(request.getContent());
            inquiry.setUpdatedAt(LocalDateTime.now());

            InquiryResponseDTO result = toResponseDTO(inquiry);
            log.info("문의 수정 완료 - 문의 ID: {}", inquiryId);
            
            return result;
            
        } catch (AccessDeniedException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            log.error("문의 수정 중 오류 발생 - 사용자 ID: {}, 문의 ID: {}, 오류: {}", userId, inquiryId, e.getMessage(), e);
            throw new RuntimeException("문의 수정 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 관리자용 문의 삭제
     * @param inquiryId 문의 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean deleteInquiryByAdmin(Long inquiryId) {
        log.info("관리자 문의 삭제 - 문의 ID: {}", inquiryId);
        
        try {
            if (!inquiryRepository.existsById(inquiryId)) {
                log.warn("삭제할 문의가 존재하지 않음 - 문의 ID: {}", inquiryId);
                return false;
            }
            
            inquiryRepository.deleteById(inquiryId);
            log.info("관리자 문의 삭제 완료 - 문의 ID: {}", inquiryId);
            return true;
            
        } catch (Exception e) {
            log.error("관리자 문의 삭제 중 오류 발생 - 문의 ID: {}, 오류: {}", inquiryId, e.getMessage(), e);
            throw new RuntimeException("문의 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자용 문의 삭제 (본인 문의만)
     * @param inquiryId 문의 ID
     * @param uno 사용자 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean deleteInquiryByUser(Long inquiryId, Long uno) {
        log.info("사용자 문의 삭제 - 문의 ID: {}, 사용자 ID: {}", inquiryId, uno);
        
        try {
            Optional<Inquiry> inquiryOpt = inquiryRepository.findById(inquiryId);
            if (inquiryOpt.isEmpty()) {
                log.warn("삭제할 문의가 존재하지 않음 - 문의 ID: {}", inquiryId);
                return false;
            }
            
            Inquiry inquiry = inquiryOpt.get();

            // 권한 확인
            if (!inquiry.getUser().getUno().equals(uno)) {
                log.warn("문의 삭제 권한 없음 - 요청자 ID: {}, 문의 작성자 ID: {}", uno, inquiry.getUser().getUno());
                return false;
            }

            inquiryRepository.delete(inquiry);
            log.info("사용자 문의 삭제 완료 - 문의 ID: {}", inquiryId);
            return true;
            
        } catch (Exception e) {
            log.error("사용자 문의 삭제 중 오류 발생 - 문의 ID: {}, 사용자 ID: {}, 오류: {}", inquiryId, uno, e.getMessage(), e);
            throw new RuntimeException("문의 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파트너용 문의 삭제 (본인에게 온 문의만)
     * @param inquiryId 문의 ID
     * @param partnerUno 파트너 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean deleteInquiryByPartner(Long inquiryId, Long partnerUno) {
        log.info("파트너 문의 삭제 - 문의 ID: {}, 파트너 ID: {}", inquiryId, partnerUno);
        
        try {
            Optional<Inquiry> inquiryOpt = inquiryRepository.findById(inquiryId);
            if (inquiryOpt.isEmpty()) {
                log.warn("삭제할 문의가 존재하지 않음 - 문의 ID: {}", inquiryId);
                return false;
            }
            
            Inquiry inquiry = inquiryOpt.get();

            // 권한 확인
            if (inquiry.getPartner() == null || !inquiry.getPartner().getUno().equals(partnerUno)) {
                log.warn("파트너 문의 삭제 권한 없음 - 요청자 ID: {}, 문의 파트너 ID: {}", 
                        partnerUno, inquiry.getPartner() != null ? inquiry.getPartner().getUno() : "null");
                return false;
            }

            inquiryRepository.delete(inquiry);
            log.info("파트너 문의 삭제 완료 - 문의 ID: {}", inquiryId);
            return true;
            
        } catch (Exception e) {
            log.error("파트너 문의 삭제 중 오류 발생 - 문의 ID: {}, 파트너 ID: {}, 오류: {}", inquiryId, partnerUno, e.getMessage(), e);
            throw new RuntimeException("문의 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자-파트너 간 문의 조회
     * @param userUno 사용자 ID
     * @param partnerUno 파트너 ID
     * @return 문의 목록
     */
    public List<Inquiry> getUserToPartnerInquiries(Long userUno, Long partnerUno) {
        log.info("사용자-파트너 문의 조회 - 사용자 ID: {}, 파트너 ID: {}", userUno, partnerUno);
        
        try {
            List<Inquiry> inquiries = inquiryRepository.findByUser_UnoAndPartner_Uno(userUno, partnerUno);
            log.info("사용자-파트너 문의 조회 완료 - 개수: {}", inquiries.size());
            return inquiries;
            
        } catch (Exception e) {
            log.error("사용자-파트너 문의 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("문의 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파트너에게 온 문의 조회
     * @param partnerUno 파트너 ID
     * @return 문의 응답 DTO 목록
     */
    public List<InquiryResponseDTO> getInquiriesToPartner(Long partnerUno) {
        log.info("파트너 문의 조회 - 파트너 ID: {}", partnerUno);
        
        try {
            List<Inquiry> inquiries = inquiryRepository.findByPartner_Uno(partnerUno);
            List<InquiryResponseDTO> result = inquiries.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            
            log.info("파트너 문의 조회 완료 - 개수: {}", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("파트너 문의 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("문의 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파트너에게 온 특정 문의 유형 문의 조회
     * @param partnerUno 파트너 ID
     * @param inquiryType 문의 유형
     * @return 문의 응답 DTO 목록
     */
    public List<InquiryResponseDTO> getInquiriesToPartnerByInquiryType(Long partnerUno, String inquiryType) {
        log.info("파트너 문의 조회 (문의유형 필터) - 파트너 ID: {}, 문의유형: {}", partnerUno, inquiryType);
        
        try {
            kroryi.dagon.enums.InquiryType inquiryTypeEnum = null;
            if (inquiryType != null && !inquiryType.trim().isEmpty()) {
                try {
                    inquiryTypeEnum = kroryi.dagon.enums.InquiryType.valueOf(inquiryType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 inquiryType: {}", inquiryType);
                    return new ArrayList<>();
                }
            }
            
            List<Inquiry> inquiries;
            if (inquiryTypeEnum != null) {
                inquiries = inquiryRepository.findByPartner_UnoAndInquiryType(partnerUno, inquiryTypeEnum);
            } else {
                inquiries = inquiryRepository.findByPartner_Uno(partnerUno);
            }
            
            List<InquiryResponseDTO> result = inquiries.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            
            log.info("파트너 문의 조회 완료 (문의유형 필터) - 개수: {}", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("파트너 문의 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("문의 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자별 문의 목록 조회
     * @param userUno 사용자 ID
     * @return 문의 응답 DTO 목록
     */
    public List<InquiryResponseDTO> getInquiriesByUserUno(Long userUno) {
        log.info("사용자 문의 목록 조회 - 사용자 ID: {}", userUno);
        
        try {
            List<Inquiry> inquiries = inquiryRepository.findByUser_Uno(userUno);
            List<InquiryResponseDTO> result = inquiries.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            
            log.info("사용자 문의 목록 조회 완료 - 개수: {}", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("사용자 문의 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("문의 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자별 특정 문의 유형 문의 목록 조회
     * @param userUno 사용자 ID
     * @param inquiryType 문의 유형
     * @return 문의 응답 DTO 목록
     */
    public List<InquiryResponseDTO> getInquiriesByUserUnoAndInquiryType(Long userUno, String inquiryType) {
        log.info("사용자 문의 목록 조회 (문의유형 필터) - 사용자 ID: {}, 문의유형: {}", userUno, inquiryType);
        
        try {
            kroryi.dagon.enums.InquiryType inquiryTypeEnum = null;
            if (inquiryType != null && !inquiryType.trim().isEmpty()) {
                try {
                    inquiryTypeEnum = kroryi.dagon.enums.InquiryType.valueOf(inquiryType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 inquiryType: {}", inquiryType);
                    return new ArrayList<>();
                }
            }
            
            List<Inquiry> inquiries;
            if (inquiryTypeEnum != null) {
                inquiries = inquiryRepository.findByUser_UnoAndInquiryType(userUno, inquiryTypeEnum);
            } else {
                inquiries = inquiryRepository.findByUser_Uno(userUno);
            }
            
            List<InquiryResponseDTO> result = inquiries.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            
            log.info("사용자 문의 목록 조회 완료 (문의유형 필터) - 개수: {}", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("사용자 문의 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("문의 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파트너 미답변 문의 개수 조회
     * @param partnerUno 파트너 ID
     * @return 미답변 문의 개수
     */
    public Long getUnansweredInquiryCount(Long partnerUno) {
        log.info("파트너 미답변 문의 개수 조회 - 파트너 ID: {}", partnerUno);
        
        try {
            Long count = inquiryRepository.countByPartner_UnoAndIsAnsweredFalse(partnerUno);
            log.info("파트너 미답변 문의 개수 조회 완료 - 개수: {}", count);
            return count;
            
        } catch (Exception e) {
            log.error("파트너 미답변 문의 개수 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("미답변 문의 개수 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파트너 특정 문의 유형 미답변 문의 개수 조회
     * @param partnerUno 파트너 ID
     * @param inquiryType 문의 유형
     * @return 미답변 문의 개수
     */
    public Long getUnansweredInquiryCountByInquiryType(Long partnerUno, String inquiryType) {
        log.info("파트너 미답변 문의 개수 조회 (문의유형 필터) - 파트너 ID: {}, 문의유형: {}", partnerUno, inquiryType);
        
        try {
            kroryi.dagon.enums.InquiryType inquiryTypeEnum = null;
            if (inquiryType != null && !inquiryType.trim().isEmpty()) {
                try {
                    inquiryTypeEnum = kroryi.dagon.enums.InquiryType.valueOf(inquiryType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 inquiryType: {}", inquiryType);
                    return 0L;
                }
            }
            
            Long count;
            if (inquiryTypeEnum != null) {
                count = inquiryRepository.countByPartner_UnoAndInquiryTypeAndIsAnsweredFalse(partnerUno, inquiryTypeEnum);
            } else {
                count = inquiryRepository.countByPartner_UnoAndIsAnsweredFalse(partnerUno);
            }
            
            log.info("파트너 미답변 문의 개수 조회 완료 (문의유형 필터) - 개수: {}", count);
            return count;
            
        } catch (Exception e) {
            log.error("파트너 미답변 문의 개수 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("미답변 문의 개수 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 문의 엔티티를 응답 DTO로 변환
     * @param inquiry 문의 엔티티
     * @return 문의 응답 DTO
     */
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
                .answerContent(inquiry.getAnswerContent())
                .isAnswered(inquiry.isAnswered())
                .build();
    }
}
