package kroryi.dagon.service.approval;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.enums.ApplicationStatus;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.PartnerApplicationRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import kroryi.dagon.entity.User;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PartnerApplicationService {

    private final PartnerApplicationRepository partnerApplicationRepository;
private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;


    public void register(PartnerApplicationDTO dto) {
        // 유저 정보 조회
        User user = userRepository.findByUno(dto.getUno())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        PartnerApplication entity = new PartnerApplication();
        entity.setPname(dto.getPname());
        entity.setCeoName(dto.getCeoName());
        entity.setPAddress(dto.getPaddress());
        entity.setPInfo(dto.getPinfo());
        entity.setLicense(dto.getLicense());
        entity.setPStatus(ApplicationStatus.PENDING); // 기본 상태
        entity.setUser(user);

        partnerApplicationRepository.save(entity);
    }

    public Page<PartnerApplicationDTO> getAllApplications(Pageable pageable) {
        return partnerApplicationRepository.findAllWithUser(pageable)
                .map(entity -> new PartnerApplicationDTO(
                        entity.getPid(),
                        entity.getUser().getUno(),
                        entity.getUser().getUid(),
                        entity.getPname(),
                        entity.getPAddress(),
                        entity.getCeoName(),
                        entity.getPInfo(),
                        entity.getLicense(),
                        entity.getPStatus().toString(),
                        entity.getPReviewedAt(),
                        entity.getPRejectionReason(),
                        entity.getUser().getUname(),
                        entity.getUser().getDisplayName()
                ));
    }

    public Page<PartnerApplicationDTO> searchApplications(String type, String keyword, String status, Pageable pageable) {
        String pname = null;
        String uname = null;

        if ("pname".equals(type)) {
            pname = keyword;
        } else if ("uname".equals(type)) {
            uname = keyword;
        }

        ApplicationStatus enumStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                enumStatus = ApplicationStatus.from(status); // ✅ 안전하게 변환
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("유효하지 않은 상태값입니다: " + status);
            }
        }

        return partnerApplicationRepository.searchWithConditions(pname, uname, enumStatus, pageable)
                .map(entity -> new PartnerApplicationDTO(
                        entity.getPid(),
                        entity.getUser().getUno(),
                        entity.getUser().getUid(),
                        entity.getPname(),
                        entity.getPAddress(),
                        entity.getCeoName(),
                        entity.getPInfo(),
                        entity.getLicense(),
                        entity.getPStatus().getKorean(), // 한글로 출력
                        entity.getPReviewedAt(),
                        entity.getPRejectionReason(),
                        entity.getUser().getUname(),
                        entity.getUser().getDisplayName()
                ));
    }



    public PartnerApplicationDTO findById(Long id) {
        PartnerApplication entity = partnerApplicationRepository.findByIdWithUser(id)
                .orElseThrow(() -> new EntityNotFoundException("신청서가 존재하지 않습니다."));

        return new PartnerApplicationDTO(
                entity.getPid(),
                entity.getUser().getUno(),
                entity.getUser().getUid(),
                entity.getPname(),
                entity.getPAddress(),
                entity.getCeoName(),
                entity.getPInfo(),
                entity.getLicense(),
                entity.getPStatus().getKorean(), // ApplicationStatus -> 한글 문자열 변환
                entity.getPReviewedAt(),
                entity.getPRejectionReason(),
                entity.getUser().getUname(),
                entity.getUser().getDisplayName()
        );
    }

    @Transactional
    public void approve(Long id) {
        PartnerApplication app = partnerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("신청 정보 없음"));

        System.out.println("== 트랜잭션 상태: " + TransactionSynchronizationManager.isActualTransactionActive());
        app.setPStatus(ApplicationStatus.APPROVED);
        app.setPReviewedAt(LocalDateTime.now());

        User user = app.getUser();
        Long uno = app.getUser().getUno();

        Partner partner = partnerRepository.findById(uno).orElse(null);

        user.setRole(UserRole.PARTNER);

        if (partner == null) {
            partner = new Partner();
            partner.setUno(uno);
            partner.setUser(app.getUser());
        }

        // 업데이트 또는 신규 저장에 공통으로 적용
        partner.setPname(app.getPname());
        partner.setPAddress(app.getPAddress());
        partner.setCeoName(app.getCeoName());
        partner.setPInfo(app.getPInfo());
        partner.setLicense(app.getLicense());

        System.out.println("uno: " + uno);
        System.out.println("partner 존재 여부: " + (partner != null));
        System.out.println("== 트랜잭션 상태: " + TransactionSynchronizationManager.isActualTransactionActive());

        partnerRepository.save(partner);
        partnerRepository.flush(); // 강제 커밋
    }

    @Transactional
    public void reject(Long id, String reason) {
        PartnerApplication app = partnerApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("신청 정보 없음"));

        app.setPStatus(ApplicationStatus.REJECTED);
        app.setPReviewedAt(LocalDateTime.now());
        app.setPRejectionReason(reason);
    }
}

