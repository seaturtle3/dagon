//package kroryi.dagon.controller.apiController;
//
//import kroryi.dagon.DTO.ProductDTO;
//import kroryi.dagon.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/product")
//public class ProductRestController {
//
//    private final ProductService productService;
//
//    @PostMapping("/api/products/sample")
//    public String addSampleProduct() {
//        productService.addSampleProductData();
//        return "샘플 추가 완료";
//    }
//
//    @GetMapping("api/products")
//    public List<ProductDTO> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//}
