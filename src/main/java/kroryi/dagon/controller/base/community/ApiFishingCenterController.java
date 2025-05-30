package kroryi.dagon.controller.base.community;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.service.community.fishingCenter.ApiFishingCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fishing-center")
public class ApiFishingCenterController {

    private final ApiFishingCenterService apiFishingCenterService;

    // API 조황정보, 조행기 중 하나라도 데이터가 있는 상품만 나오게
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

}
