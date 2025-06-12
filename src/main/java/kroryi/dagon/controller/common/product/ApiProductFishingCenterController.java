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
        ApiFishingReportDTO report = Optional.ofNullable(apiFishingReportService.getByProductId(productId))
                .orElse(null);
        ApiFishingDiaryDTO diary = Optional.ofNullable(apiFishingDiaryService.getByProductId(productId))
                .orElse(null);

        if (report == null && diary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조황정보와 조행기 모두 없습니다.");
        }

        return new ApiFishingCenterDTO(report, diary);
    }

    // 특정 배 상품 ID 조황정보 조회
    @Operation(summary = "제품 ID로 조황정보 조회")
    @GetMapping("/fishing-report/{productId}")
    public ApiFishingReportDTO getFishingReportByProductId(@PathVariable Long productId) {
        ApiFishingReportDTO report = apiFishingReportService.getByProductId(productId);

        if (report == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조황정보가 없습니다.");
        }

        return report;
    }

    // 특정 배 상품 ID 조행기 조회
    @Operation(summary = "제품 ID로 조행기 조회")
    @GetMapping("/fishing-diary/{productId}")
    public ApiFishingDiaryDTO getFishingDiaryByProductId(@PathVariable Long productId) {
        ApiFishingDiaryDTO diary = apiFishingDiaryService.getByProductId(productId);

        if (diary == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조행기가 없습니다.");
        }

        return diary;
    }

}