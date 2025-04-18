package kroryi.dagon.service;

import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnersRepository;

    public List<PartnerDTO> getAllPartners() {
        return partnersRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
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

}
