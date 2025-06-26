package kroryi.dagon.service.auth;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.ApplicationStatus;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.*;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import kroryi.dagon.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import kroryi.dagon.repository.FreeBoardRepository;
import kroryi.dagon.repository.FreeBoardCommentRepository;
import kroryi.dagon.repository.FishingDiaryCommentRepository;
import kroryi.dagon.repository.FishingReportCommentRepository;
import kroryi.dagon.repository.InquiryRepository;
import kroryi.dagon.repository.NotificationRepository;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.fishingCenter.FishingDiary;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerApplicationRepository partnerApplicationRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnersRepository;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final FishingReportRepository fishingReportRepository;
    private final FishingDiaryRepository fishingDiaryRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardCommentRepository freeBoardCommentRepository;
    private final FishingDiaryCommentRepository fishingDiaryCommentRepository;
    private final FishingReportCommentRepository fishingReportCommentRepository;
    private final InquiryRepository inquiryRepository;
    private final NotificationRepository notificationRepository;
    private final ReportRepository reportRepository;



    // 파트너 신청 적용
    @Transactional
    public void applyPartner(PartnerApplicationDTO dto) {
        // User 엔티티 조회
        User user = userRepository.findById(dto.getUno())
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        // DTO → 엔티티 변환
        PartnerApplication entity = new PartnerApplication();

        entity.setPname(dto.getPname());
        entity.setCeoName(dto.getCeoName());
        entity.setPAddress(dto.getPaddress());
        entity.setPInfo(dto.getPinfo());
        entity.setLicense(dto.getLicense());
        entity.setPStatus(ApplicationStatus.PENDING);
        entity.setUser(user);

        // 저장
        partnerApplicationRepository.save(entity);
    }


    public Partner findByUno(String uno) {
        return (Partner) partnersRepository.findByUno(Long.valueOf(uno))
                .orElseThrow(() -> new RuntimeException("Partner not found for uno: " + uno));
    }

    public Page<PartnerDTO> getAllPartners(int page, int size, String keyword, String searchType) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return partnersRepository.findAll(pageable).map(PartnerDTO::new);
        }

        switch (searchType) {
            case "ceoName":
                return partnersRepository.findByCeoNameContainingIgnoreCase(keyword, pageable).map(PartnerDTO::new);
            case "paddress":
            case "pAddress": // 혹시 프론트에서 paddress로 오면 pAddress로 매핑
                return partnersRepository.findBypAddressContainingIgnoreCase(keyword, pageable).map(PartnerDTO::new);
            case "pname":
            default:
                return partnersRepository.findByPnameContainingIgnoreCase(keyword, pageable).map(PartnerDTO::new);
        }
    }

    private PartnerDTO toDTO(Partner partner) {
        return new PartnerDTO(
                partner.getUno(),
                partner.getPname(),
                partner.getPAddress(),
                partner.getCeoName(),
                partner.getPInfo(),
                partner.getLicense(),
                partner.getLicenseImg()
        );
    }


    public PartnerDTO getPartnerById(Long id) {
        Partner partner = partnersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파트너를 찾을 수 없습니다."));
        return convertToDTO(partner);
    }


    public PartnerDTO updatePartner(long id, PartnerDTO partnerDTO) {
        Partner partner = partnersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파트너를 찾을 수 없습니다."));

        // 업데이트할 값 세팅
        partner.setPname(partnerDTO.getPname());
        partner.setPAddress(partnerDTO.getPAddress());
        partner.setCeoName(partnerDTO.getCeoName());
        partner.setPInfo(partnerDTO.getPInfo());
        partner.setLicense(partnerDTO.getLicense());
        partner.setLicenseImg(partnerDTO.getLicenseImg());

        Partner updatedPartner = partnersRepository.save(partner);
        return convertToDTO(updatedPartner);
    }

    private PartnerDTO convertToDTO(Partner partner) {
        PartnerDTO dto = new PartnerDTO();
        dto.setUno(partner.getUno());
        dto.setPname(partner.getPname());
        dto.setPAddress(partner.getPAddress());
        dto.setCeoName(partner.getCeoName());
        dto.setPInfo(partner.getPInfo());
        dto.setLicense(partner.getLicense());
        dto.setLicenseImg(partner.getLicenseImg());
        return dto;
    }

    public void deletePartner(long id) {
        Partner partner = partnersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파트너를 찾을 수 없습니다."));

        partnersRepository.delete(partner);
    }

    // PartnersService.java
    public Partner getDefaultPartner() {
        return partnersRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("기본 파트너가 없습니다."));  // 기본 파트너가 없으면 예외 처리
    }




    @Transactional
    public PartnerDTO MypageUpdatePartner(Long id, PartnerDTO partnerDTO) {
        Partner partner = partnersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파트너를 찾을 수 없습니다."));

        partner.setPname(partnerDTO.getPname());
        partner.setCeoName(partnerDTO.getCeoName());
        partner.setPAddress(partnerDTO.getPAddress());
        partner.setPInfo(partnerDTO.getPInfo());
        partner.setLicense(partnerDTO.getLicense());
        partner.setLicenseImg(partnerDTO.getLicenseImg());

        partnersRepository.save(partner);

        return new PartnerDTO(partner);
    }


    public Partner findPartnerByUserUno(Long uno) {
        return partnersRepository.findByUserUno(uno)
                .orElse(null); // 파트너 정보가 없으면 null 반환
    }

    public Partner findPartnerById(Long id) {
        return partnersRepository.findById(id).orElse(null);
    }


    @Autowired
    private EntityManager em;

    @Transactional
    public void deletePartner(Long partnerId) {
        Partner partner = partnersRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("파트너를 찾을 수 없습니다."));

        User user = partner.getUser();

        // 1. 조황정보, 조황정보 댓글, 조행기, 조행기 댓글, 예약, 상품 순서로 삭제
        // 1-1. 조황정보 및 댓글
        List<FishingReport> reports = fishingReportRepository.findByProduct_Partner_Uno(partnerId);
        for (FishingReport report : reports) {
            fishingReportCommentRepository.deleteAll(fishingReportCommentRepository.findByFishingReport_FrId(report.getFrId()));
        }
        fishingReportRepository.deleteAll(reports);

        // 1-2. 조행기 및 댓글
        List<FishingDiary> diaries = fishingDiaryRepository.findByProduct_Partner_Uno(partnerId);
        for (FishingDiary diary : diaries) {
            fishingDiaryCommentRepository.deleteAll(fishingDiaryCommentRepository.findByFishingDiary_FdId(diary.getFdId()));
        }
        fishingDiaryRepository.deleteAll(diaries);

        // 1-3. 예약 삭제
        reservationRepository.deleteAll(reservationRepository.findByProduct_Partner_Uno(partnerId));
        // 1-4. 상품 삭제
        productRepository.deleteAll(productRepository.findByPartner_Uno(partnerId));

        // 기타 댓글, 알림, 신고 등도 필요시 삭제
        freeBoardCommentRepository.deleteAll(freeBoardCommentRepository.findByUserUno(user.getUno()));
        inquiryRepository.deleteAllByUser_Uno(user.getUno());
        notificationRepository.deleteAllByReceiver_Uno(user.getUno());
        reportRepository.deleteAllByReporter_UnoOrReported_Uno(user.getUno(), user.getUno());

        // 2. User와 Partner 연관관계 끊기
        if (user != null) {
            user.setPartner(null);
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }

        // 3. 파트너 삭제
        partnersRepository.delete(partner);
    }

    /**
     * 파트너 삭제 가능 여부를 확인하는 메서드
     * @param partnerId 파트너 ID
     * @return 삭제 가능 여부와 메시지
     */
    public DeleteCheckResult checkPartnerDeletion(Long partnerId) {
        Partner partner = partnersRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("파트너를 찾을 수 없습니다."));

        Long reservationCount = reservationRepository.countByProduct_Partner_Uno(partnerId);
        Long productCount = productRepository.countByPartner_UnoAndDeletedFalse(partnerId);

        boolean canDelete = true;
        String message = "삭제 가능합니다.";

        if (reservationCount > 0) {
            canDelete = false;
            message = String.format("예약 내역이 %d건 남아있어 삭제할 수 없습니다. 모든 예약을 먼저 삭제하세요.", reservationCount);
        } else if (productCount > 0) {
            // 상품이 있어도 삭제 가능 (CASCADE로 함께 삭제됨)
            message = String.format("상품 %d건이 함께 삭제됩니다.", productCount);
        }

        return new DeleteCheckResult(canDelete, message, reservationCount, productCount);
    }

    /**
     * 파트너 삭제 확인 결과를 담는 내부 클래스
     */
    public static class DeleteCheckResult {
        private final boolean canDelete;
        private final String message;
        private final Long reservationCount;
        private final Long productCount;

        public DeleteCheckResult(boolean canDelete, String message, Long reservationCount, Long productCount) {
            this.canDelete = canDelete;
            this.message = message;
            this.reservationCount = reservationCount;
            this.productCount = productCount;
        }

        public boolean isCanDelete() { return canDelete; }
        public String getMessage() { return message; }
        public Long getReservationCount() { return reservationCount; }
        public Long getProductCount() { return productCount; }
    }

    public boolean isOwner(Long uno, Long partnerId) {
        Optional<Partner> optionalPartner = partnersRepository.findById(partnerId);
        if (optionalPartner.isEmpty()) return false;

        Partner partner = optionalPartner.get();
        if (partner.getUser() == null) return false;

        return uno.equals(partner.getUser().getUno());
    }


    public Page<Partner> searchPartners(String searchType, String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return partnersRepository.findAll(pageable);
        }

        String likeKeyword = "%" + keyword.trim() + "%";

        switch (searchType) {
            case "ceoName":
                return partnersRepository.findByCeoNameContaining(likeKeyword, pageable);
            case "pAddress":
                return partnersRepository.findByPAddressContaining(likeKeyword, pageable);
            case "pname":
            default:
                return partnersRepository.findByPnameContaining(likeKeyword, pageable);
        }
    }



    public Long getCurrentPartnerId() {
        String token = jwtUtil.resolveToken(request);
        return jwtUtil.getUnoFromToken(token);
    }

    @Transactional
    public Long getReservationCountByPartnerId(Long partnerId) {
        return reservationRepository.countByProduct_Partner_Uno(partnerId);
    }

    @Transactional
    public Long getTodayReservationCountByPartnerId(Long partnerId) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return reservationRepository.countByProduct_Partner_UnoAndFishingAtBetween(partnerId, startOfDay, endOfDay);
    }

    @Transactional
    public List<Reservation> getRecentReservationsByPartnerId(Long partnerId) {
        return reservationRepository.findTop3ByProduct_Partner_UnoOrderByCreatedAtDesc(partnerId);
    }
}


