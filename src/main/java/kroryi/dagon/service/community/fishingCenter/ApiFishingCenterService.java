package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiFishingCenterService {

    private final ProductRepository productRepository;

    // API 조황정보, 조행기 둘 중 하나라도 데이터가 있는가
    public Page<ProductDTO> getProductsWithReportsOrDiaries(Pageable pageable) {
        Page<Product> products = productRepository.findAllWithNonEmptyReportOrDiary(pageable);
        return products.map(ProductDTO::fromEntity);
    }

}
