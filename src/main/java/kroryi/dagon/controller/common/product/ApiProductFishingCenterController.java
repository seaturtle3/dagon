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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "제품 ID로 조황센터 정보 조회")
@RequestMapping("api/product")
public class ApiProductFishingCenterController {

    private final ApiFishingReportService apiFishingReportService;
    private final ApiFishingDiaryService apiFishingDiaryService;

    // 특정 상품 ID 조황정보, 조행기 전체 조회
    @Operation(summary = "제품 ID로 조황센터 정보 조회")
    @GetMapping("/product-fishing-center/{productId}")
    public ApiFishingCenterDTO getFishingCenterByProductId(@PathVariable Long productId) {
        ApiFishingReportDTO report = apiFishingReportService.getByProductId(productId);
        ApiFishingDiaryDTO diary = apiFishingDiaryService.getByProductId(productId);

        return new ApiFishingCenterDTO(report, diary);
    }

}