package kroryi.dagon.controller.api;

import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.service.PartnerApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


// 파트너 심사 게시판 컨트롤

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerApplicationApiController {

    private final PartnerApplicationService partnerApplicationService;

    @GetMapping
    public Page<PartnerApplicationDTO> listApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return partnerApplicationService.searchApplications(type, keyword, status, pageable);
    }
}