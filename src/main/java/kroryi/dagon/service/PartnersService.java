package kroryi.dagon.service;

import kroryi.dagon.entity.Partner;
import kroryi.dagon.repository.PartnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnersService {

    private final PartnersRepository partnersRepository;

    public List<Partner> getAllPartners() {
        return partnersRepository.findAll();
    }


}
