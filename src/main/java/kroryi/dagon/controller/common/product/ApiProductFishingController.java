package kroryi.dagon.controller.common.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 어종 조회 API (공용)")
@RequestMapping("api/product-fish-species")
public class ApiProductFishingController {

    // 특정 상품 ID 조황정보, 조행기 전체 조회


}