package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
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
public class PartnersApiController {

    private final PartnersService partnersService;

    @Operation(summary = "모든 파트너 정보 조회", description = "모든 파트너 정보 조회")
    @GetMapping("/all")
    public List<PartnerDTO> getAllPartners() {
        return partnersService.getAllPartners();
    }



}
