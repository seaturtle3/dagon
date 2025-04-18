package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/partners")
public class ApiPartnerController {

    private final PartnerService partnerService;

    @Operation(summary = "모든 파트너 정보 조회", description = "모든 파트너 정보 조회")
    @GetMapping("/all")
    public List<PartnerDTO> getAllPartners() {
        return partnerService.getAllPartners();
    }

    @Operation(summary = "파트너 정보 업데이트", description = "파트너 정보 업데이트")
    @PutMapping("/update/{id}")
    public PartnerDTO updatePartner(@PathVariable long id, @RequestBody PartnerDTO partnerDTO) {
        return partnerService.updatePartner(id, partnerDTO);
    }


}
