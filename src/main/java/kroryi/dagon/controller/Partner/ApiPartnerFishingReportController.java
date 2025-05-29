package kroryi.dagon.controller.Partner;


import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.service.PartnerFishingReportService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/fishing-report")
@RequiredArgsConstructor
public class ApiPartnerFishingReportController {

    private final PartnerFishingReportService partnerFishingReportService;
    private final JwtUtil jwtUtil;


    @GetMapping("/mine")
    public List<FishingReportDTO> getMyReports(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        return partnerFishingReportService.getMyReports(uno);
    }


    @GetMapping("/{frId}")
    public FishingReportDTO getMyReport(@PathVariable Long frId, HttpServletRequest request) throws AccessDeniedException {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        return partnerFishingReportService.getMyReport(frId, uno);
    }

    @PutMapping("/{frId}")
    public ResponseEntity<?> updateMyReport(
            @PathVariable Long frId,
            @RequestPart("dto") FishingReportDTO dto,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            HttpServletRequest request) throws AccessDeniedException {

        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);

        FishingReportDTO updatedReport = partnerFishingReportService.updateMyReportWithFile(frId, uno, dto, thumbnailFile);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{frId}")
    public void deleteMyReport(@PathVariable Long frId, HttpServletRequest request) throws AccessDeniedException {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        partnerFishingReportService.deleteMyReport(frId, uno);
    }
}

