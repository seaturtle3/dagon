package kroryi.dagon.service;

import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnersRepository partnersRepository;

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
