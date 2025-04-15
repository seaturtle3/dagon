package kroryi.dagon.service;

import kroryi.dagon.DTO.ProductOptionDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.ProductOption;
import kroryi.dagon.repository.ProductOptionRepository;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public List<ProductOptionDTO> getAllProductOptions() {
        List<ProductOption> options = productOptionRepository.findAll();
        List<ProductOptionDTO> dtos = new ArrayList<>();

        for (ProductOption option : options) {
            ProductOptionDTO dto = new ProductOptionDTO();
            dto.setOptId(option.getOptId());
            dto.setOptName(option.getOptName());
            dto.setOptTime(option.getOptTime());
            dto.setOptDescription(option.getOptDescription());
            dto.setPrice(option.getPrice());
            dto.setProdId(option.getProduct().getProdId()); // prod_id 그대로

            // prod_name 조회
            String prodName = productRepository.findById(option.getProduct().getProdId())
                    .map(Product::getProdName)
                    .orElse("상품 없음");
            dto.setProdName(prodName);

            dtos.add(dto);
        }

        return dtos;
    }

}
