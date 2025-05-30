package kroryi.dagon.controller.Partner;

import io.swagger.v3.oas.annotations.Operation;

import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.DTO.PartnerPageDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.PartnerService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//            파트너 등록 컨트롤
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("api/partner")
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

    @PutMapping("my_page/update/{id}")
    @Operation(summary = "파트너 수정", description = "ID로 파트너 정보를 수정합니다.")
    public ResponseEntity<PartnerDTO> updatePartner(
            @PathVariable Long id,
            @RequestBody PartnerDTO partnerDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!userDetails.getRole().equalsIgnoreCase("ADMIN")
                && !partnerService.isOwner(userDetails.getUno(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PartnerDTO updatedPartner = partnerService.MypageUpdatePartner(id, partnerDTO);
        log.info("userDetails uno: {}, role: {}, path id: {}", userDetails.getUno(), userDetails.getRole(), id);
        return ResponseEntity.ok(updatedPartner);
    }


    @Operation(summary = "파트너 삭제", description = "파트너 삭제 (ADMIN은 ID로 삭제 가능)")
    @DeleteMapping("my_page/{id}")
    public ResponseEntity<?> deletePartner(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long id) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        String role = userDetails.getRole(); // "USER", "PARTNER", "ADMIN"
        Long loginUserUno = userDetails.getUno();



        // 1. 관리자면 어떤 파트너든 삭제 가능
        if ("ADMIN".equalsIgnoreCase(role)) {
            partnerService.deletePartner(id);
            return ResponseEntity.ok().build();
        }

        // 2. 일반 사용자면 자신의 uno와 매칭된 파트너만 삭제 가능
        Partner partner = partnerService.findPartnerById(id);
        if (partner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 파트너를 찾을 수 없습니다.");
        }

        if (!partner.getUser().getUno().equals(loginUserUno)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인의 파트너 정보만 삭제할 수 있습니다.");
        }

        partnerService.deletePartner(id);
        return ResponseEntity.ok().build();
    }


}


