package kroryi.dagon.component;

import kroryi.dagon.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductService productService;

    public ProductDataLoader(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        productService.addSampleProductData(); // 데이터 추가
    }
}
