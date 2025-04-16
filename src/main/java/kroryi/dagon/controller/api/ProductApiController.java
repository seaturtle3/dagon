package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {

    private final ProductService productService;

    @Operation(summary = "모든 상품 조회", description = "모든 상품 조회")
    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

}
