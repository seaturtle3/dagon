package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.service.board.fishingReportDiary.ApiFishingReportService;
import kroryi.dagon.service.board.fishingReportDiary.FishingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board-Community", description = "조황정보 게시판 글쓰기/수정/삭제 API")
@RequestMapping("/api/fishing-report")
public class ApiFishingReportController {

    private final ApiFishingReportService apiFishingReportService;

    @Operation(summary = "조황정보 생성")
    @PostMapping("/create")
    public FishingReportDTO createFishingReport(@RequestBody FishingReportDTO fishingReportDTO) {
        return apiFishingReportService.createFishingReport(fishingReportDTO);
    }

    @Operation(summary = "조황정보 모두 조회")
    @GetMapping("/get-all")
    public List<FishingReportDTO> getAllFishingReport() {
        return apiFishingReportService.getAllFishingReports();
    }

    @Operation(summary = "조황정보 ID 조회")
    @GetMapping("/get/{id}")
    public FishingReport getFishingReport(@PathVariable Long id) {
        return apiFishingReportService.getFishingReportById(id);
    }

    @Operation(summary = "조황정보 수정")
    @PutMapping("/update/{id}")
    public Long updateFishingReport(@PathVariable("id") Long frId,
                                    @RequestBody FishingReportDTO fishingReportDTO) {
        return apiFishingReportService.updateFishingReport(frId, fishingReportDTO);
    }

    @Operation(summary = "조황정보 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingReport(@PathVariable Long id) {
        apiFishingReportService.deleteFishingReport(id);
    }

}
