package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.ProductOptionDTO;
import kroryi.dagon.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product-option")
public class ProductOptionApiController {

    private final ProductOptionService prod_optionService;

    @Operation(summary = "모든 상품 옵션 조회",description = "모든 상품 옵션 조회")
    @GetMapping("/all")
    public List<ProductOptionDTO> getAllProductOptions() {
        return prod_optionService.getAllProductOptions();
    }

}
