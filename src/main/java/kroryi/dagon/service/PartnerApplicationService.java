package kroryi.dagon.service;

import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.repository.PartnerApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import kroryi.dagon.entity.User;


@Service
@RequiredArgsConstructor
public class PartnerApplicationService {

    private final PartnerApplicationRepository partnerApplicationRepository;

    public Page<PartnerApplicationDTO> getAllApplications(Pageable pageable) {
        return partnerApplicationRepository.findAllWithUser(pageable)
                .map(entity -> new PartnerApplicationDTO(
                        entity.getPaid(),
                        entity.getUser().getUno(),
                        entity.getPname(),
                        entity.getPaddress(),
                        entity.getPceo(),
                        entity.getPinfo(),
                        entity.getPlicense(),
                        entity.getPaStatus().toString(),
                        entity.getPaCreatedAt(),
                        entity.getPaReviewedAt(),
                        entity.getPaRejectionReason(),
                        entity.getUser().getUname()
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

        // ✅ String status를 enum으로 안전하게 변환
        PartnerApplication.ApplicationStatus enumStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                enumStatus = PartnerApplication.ApplicationStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("유효하지 않은 상태값입니다: " + status);
            }
        }

        return partnerApplicationRepository.searchWithConditions(pname, uname, enumStatus, pageable)
                .map(entity -> new PartnerApplicationDTO(
                        entity.getPaid(),
                        entity.getUser().getUno(),
                        entity.getPname(),
                        entity.getPaddress(),
                        entity.getPceo(),
                        entity.getPinfo(),
                        entity.getPlicense(),
                        entity.getPaStatus().toString(),
                        entity.getPaCreatedAt(),
                        entity.getPaReviewedAt(),
                        entity.getPaRejectionReason(),
                        entity.getUser().getUname()
                ));
    }
}

