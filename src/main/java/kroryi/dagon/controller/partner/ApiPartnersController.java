package kroryi.dagon.controller.partner;

import io.swagger.v3.oas.annotations.Operation;

import kroryi.dagon.DTO.PartnerDTO;

import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Partner;

import kroryi.dagon.service.auth.AdminUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//            파트너 등록 컨트롤
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("api/partner")
public class ApiPartnersController {

    private final kroryi.dagon.service.auth.PartnerService partnerService;


    @Operation(summary = "파트너 정보 조회 (페이징 + 검색 + 타입)", description = "파트너 정보 목록을 페이징과 검색(타입별)으로 조회합니다.")
    @GetMapping("/all")
    public Page<PartnerDTO> getAllPartners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "pname") String searchType
    ) {
        return partnerService.getAllPartners(page, size, keyword, searchType);
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


    @DeleteMapping("my_page/{id}")
    public ResponseEntity<?> deletePartner(@AuthenticationPrincipal Object principal,
                                           @PathVariable Long id) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role = null;
        Long loginUserUno = null;

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            role = userDetails.getRole();
            loginUserUno = userDetails.getUno();
        } else if (principal instanceof AdminUserDetails) {
            AdminUserDetails adminDetails = (AdminUserDetails) principal;
            role = adminDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse(null);
            loginUserUno = 1L;  // 어드민 uno 고정값 예시
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            partnerService.deletePartner(id);
            return ResponseEntity.ok().build();
        }

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


