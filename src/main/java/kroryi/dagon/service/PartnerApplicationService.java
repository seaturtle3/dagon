package kroryi.dagon.service;

import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.enums.ApplicationStatus;
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
                        entity.getPid(),
                        entity.getUser().getUno(),
                        entity.getPname(),
                        entity.getPAddress(),
                        entity.getCeoName(),
                        entity.getPInfo(),
                        entity.getLicense(),
                        entity.getPStatus().toString(),
                        entity.getPReviewedAt(),
                        entity.getPRejectionReason(),
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
                        entity.getPname(),
                        entity.getPAddress(),
                        entity.getCeoName(),
                        entity.getPInfo(),
                        entity.getLicense(),
                        entity.getPStatus().getKorean(), // 한글로 출력
                        entity.getPReviewedAt(),
                        entity.getPRejectionReason(),
                        entity.getUser().getUname()
                ));
    }
}

