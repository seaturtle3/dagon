package kroryi.dagon.controller.common.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ProductOptionDTO;
import kroryi.dagon.service.product.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 옵션 조회 API (공용)")
@RequestMapping("api/product-option")
public class ApiProductOptionController {

    private final ProductOptionService prod_optionService;

    @Operation(summary = "모든 상품 옵션 조회",description = "모든 상품 옵션 조회")
    @GetMapping("/all")
    public List<ProductOptionDTO> getAllProductOptions() {
        return prod_optionService.getAllProductOptions();
    }

}
