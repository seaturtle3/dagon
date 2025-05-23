package kroryi.dagon.controller.partner.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingReportDTO;
import kroryi.dagon.service.board.fishingReportDiary.ApiFishingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingReport", description = "조황정보 API (파트너)")
@RequestMapping("/api/fishing-report")
public class ApiPartnerFishingReportController {

    private final ApiFishingReportService apiFishingReportService;

    @Operation(summary = "조황 생성")
    @PostMapping("/create")
    public ApiFishingReportDTO createFishingReport(@RequestBody ApiFishingReportDTO apiFishingReportDTO) {
        return apiFishingReportService.createFishingReport(apiFishingReportDTO);
    }

    @Operation(summary = "조황 모두 조회")
    @GetMapping("/get-all")
    public List<ApiFishingReportDTO> getAllFishingReport() {
        return apiFishingReportService.getAllFishingReports();
    }

    @Operation(summary = "조황 ID 조회")
    @GetMapping("/get/{id}")
    public ApiFishingReportDTO getFishingReport(@PathVariable Long id) {
        return apiFishingReportService.getFishingReportById(id);
    }

    @Operation(summary = "조황 수정")
    @PutMapping("/update/{id}")
    public Long updateFishingReport(@PathVariable("id") Long frId,
                                    @RequestBody ApiFishingReportDTO apiFishingReportDTO) {
        return apiFishingReportService.updateFishingReport(frId, apiFishingReportDTO);
    }

    @Operation(summary = "조황 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingReport(@PathVariable Long id) {
        apiFishingReportService.deleteFishingReport(id);
    }

}
