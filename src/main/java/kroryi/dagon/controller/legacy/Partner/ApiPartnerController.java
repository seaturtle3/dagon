package kroryi.dagon.controller.legacy.Partner;

import io.swagger.v3.oas.annotations.Operation;

import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.service.auth.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//            파트너 등록 컨트롤
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/partner")
public class ApiPartnerController {

    private final PartnerService partnerService;



    @Operation(summary = "모든 파트너 정보 조회", description = "모든 파트너 정보 조회")
    @GetMapping("/all")
    public List<PartnerDTO> getAllPartners() {
        return partnerService.getAllPartners();
    }

    @Operation(summary = "특정 파트너 정보 조회", description = "ID로 특정 파트너 정보를 조회")
    @GetMapping("/{id}")
    public PartnerDTO getPartner(@PathVariable Long id) {
        return partnerService.getPartnerById(id);
    }

    @Operation(summary = "파트너 정보 업데이트", description = "id로 파트너 찾은 후 정보 업데이트")
    @PutMapping("/update/{id}")
    public PartnerDTO updatePartner(@PathVariable long id, @RequestBody PartnerDTO partnerDTO) {
        return partnerService.updatePartner(id, partnerDTO);
    }

    @Operation(summary = "파트너 삭제", description = "ID로 파트너를 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
    }
}


