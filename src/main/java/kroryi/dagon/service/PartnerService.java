package kroryi.dagon.service;


import kroryi.dagon.DTO.PartnerApplicationDTO;;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.PartnerApplicationRepository;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;



@Log4j2
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerApplicationRepository partnerApplicationRepository;
    private final UserRepository userRepository;
    private final PartnersRepository partnersRepository;


    // 파트너 신청 적용
    public void partner(PartnerApplicationDTO dto) throws Exception {
        PartnerApplication application = new PartnerApplication();

        application.setPname(dto.getPname());
        application.setPAddress(dto.getPaddress());
        application.setCeoName(dto.getCeoName());
        application.setLicense(dto.getLicense());
        application.setPInfo(dto.getPinfo());


        User user = userRepository.findById(dto.getUno())
                .orElseThrow(() -> new Exception("사용자 정보를 찾을 수 없습니다."));
        application.setUser(user);

        partnerApplicationRepository.save(application);
    }

    public List<PartnerDTO> getAllPartners() {
        return partnersRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
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
}

