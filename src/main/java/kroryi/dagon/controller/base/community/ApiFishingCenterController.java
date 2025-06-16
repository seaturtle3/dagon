package kroryi.dagon.controller.base.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingCenterDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingReportDTO;
import kroryi.dagon.service.community.fishingCenter.ApiFishingCenterService;
import kroryi.dagon.service.community.fishingReportDiary.ApiFishingDiaryService;
import kroryi.dagon.service.community.fishingReportDiary.ApiFishingReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingCenter", description = "조황정보/조행기 조회")
@RequestMapping("/api/fishing-center")
@Log4j2
public class ApiFishingCenterController {

    private final ApiFishingCenterService apiFishingCenterService;
    private final ApiFishingReportService apiFishingReportService;
    private final ApiFishingDiaryService apiFishingDiaryService;

    // API 조황정보, 조행기 중 하나라도 데이터가 있는 상품만 나오게
    @Operation(summary = "조황정보, 조행기 중 하나라도 데이터가 있는 상품 조회")
    @GetMapping("/get-with-reports-or-diaries")
    public Page<ProductDTO> getProductsWithContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prodId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return apiFishingCenterService.getProductsWithReportsOrDiaries(pageable);
    }

    // 모든 조황정보/조행기 나열
    @Operation(summary = "전체 조황센터 정보 조회")
    @GetMapping("/get-all")
    public ApiFishingCenterDTO getAllFishingCenter() {
        List<ApiFishingReportDTO> reports = apiFishingReportService.getAll(); // productId 없이 전체 조회
        List<ApiFishingDiaryDTO> diaries = apiFishingDiaryService.getAll();

        if ((reports == null || reports.isEmpty()) && (diaries == null || diaries.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조황정보와 조행기 모두 없습니다.");
        }

        return new ApiFishingCenterDTO(reports, diaries);
    }

}
