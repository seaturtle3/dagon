package kroryi.dagon.controller.apiController;

import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.service.PartnersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/partners")
public class PartnersRestController {

    private final PartnersService partnersService;

    @GetMapping("/all")
    public List<PartnerDTO> getAllPartners() {
        return partnersService.getAllPartners();
    }



}
