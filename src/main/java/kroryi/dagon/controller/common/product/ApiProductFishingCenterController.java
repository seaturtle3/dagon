package kroryi.dagon.controller.common.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingCenterDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingReportDTO;
import kroryi.dagon.service.community.fishingCenter.ApiFishingCenterService;
import kroryi.dagon.service.community.fishingReportDiary.ApiFishingDiaryService;
import kroryi.dagon.service.community.fishingReportDiary.ApiFishingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "제품 ID로 조황센터 정보 조회")
@RequestMapping("api/product")
public class ApiProductFishingCenterController {

    private final ApiFishingReportService apiFishingReportService;
    private final ApiFishingDiaryService apiFishingDiaryService;

    // 특정 상품 ID 조황정보, 조행기 전체 조회
    @Operation(summary = "제품 ID로 조황센터 정보 조회")
    @GetMapping("/fishing-center/{productId}")
    public ApiFishingCenterDTO getFishingCenterByProductId(@PathVariable Long productId) {
        List<ApiFishingReportDTO> reports = apiFishingReportService.getAllByProductId(productId);
        List<ApiFishingDiaryDTO> diaries = apiFishingDiaryService.getAllByProductId(productId);

        if ((reports == null || reports.isEmpty()) && (diaries == null || diaries.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조황정보와 조행기 모두 없습니다.");
        }

        return new ApiFishingCenterDTO(reports, diaries);
    }


    // 특정 배 상품 ID 조황정보 조회
    @Operation(summary = "제품 ID로 조황정보 전체 조회")
    @GetMapping("/fishing-report/{productId}")
    public List<ApiFishingReportDTO> getFishingReportByProductId(@PathVariable Long productId) {
        List<ApiFishingReportDTO> reports = apiFishingReportService.getAllByProductId(productId);

        if (reports == null || reports.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조황정보가 없습니다.");
        }

        return reports;
    }

    // 특정 배 상품 ID 조행기 조회
    @Operation(summary = "제품 ID로 조행기 전체 조회")
    @GetMapping("/fishing-diary/{productId}")
    public List<ApiFishingDiaryDTO> getFishingDiaryByProductId(@PathVariable Long productId) {
        List<ApiFishingDiaryDTO> diaries = apiFishingDiaryService.getAllByProductId(productId);

        if (diaries == null || diaries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조행기가 없습니다.");
        }

        return diaries;
    }

}