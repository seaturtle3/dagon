package kroryi.dagon.service;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void addSampleProductData() {
        Product product1 = new Product();
        product1.setProdName("배 이름 1");
        product1.setProdRegion(ProdRegion.SEOUL); // 예시로 SEA 지역
        product1.setMainType(MainType.FRESHWATER); // 예시로 MainType 설정
        product1.setSubType(SubType.ARTIFICIAL);   // 예시로 SubType 설정
        product1.setMaxPerson(20);
        product1.setMinPerson(1);
        product1.setWeight(new BigDecimal("150.50"));
        product1.setProdAddress("서울");
        product1.setProdDescription("예시 배 설명");
        product1.setProdEvent("2025-04-30 10:00");
        product1.setProdNotice("특별한 공지사항");

    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion().toString());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setProdAddress(product.getProdAddress());
        // 필요한 만큼 추가
        return dto;
    }

}
