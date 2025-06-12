package kroryi.dagon.service.auth;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.ApplicationStatus;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.PartnerApplicationRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.entity.Partner;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerApplicationRepository partnerApplicationRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnersRepository;


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

    public void deletePartner(long id) {
        Partner partner = partnersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파트너를 찾을 수 없습니다."));

        partnersRepository.delete(partner);
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

        // User와 Partner 연관관계 끊기
        if (user != null) {
            user.setPartner(null);
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }

        // Partner 삭제 - cascade + orphanRemoval로 관련된 Product, 하위 엔티티도 모두 삭제됨
        partnersRepository.delete(partner);
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
}


