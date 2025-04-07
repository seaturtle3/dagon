package kroryi.dagon.service;


import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Log4j2
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerApplicationRepository;
    private final UserRepository userRepository;


    // 파트너 신청 적용
    public void partner(PartnerApplicationDTO dto) throws Exception {
        PartnerApplication application = new PartnerApplication();

        application.setPname(dto.getPname());
        application.setPaddress(dto.getPaddress());
        application.setPceo(dto.getPceo());
        application.setPlicense(dto.getPlicense());
        application.setPinfo(dto.getPinfo());
        application.setPaCreatedAt(LocalDateTime.now());

        User user = userRepository.findById(dto.getUno())
                .orElseThrow(() -> new Exception("사용자 정보를 찾을 수 없습니다."));
        application.setUser(user);

        partnerApplicationRepository.save(application);
    }
}

