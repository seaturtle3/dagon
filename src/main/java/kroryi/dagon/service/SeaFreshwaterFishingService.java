package kroryi.dagon.service;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import jakarta.persistence.criteria.Predicate;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeaFreshwaterFishingService {

    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;
    private final ProductRepository productRepository;

    public String getFindAll() {
        return seaFreshwaterFishingRepository.findAll().toString();
    }

    // 모든 상품을 지역과 메인타입으로 필터
    public List<ProductDTO> getAllProductsByRegionAndMainType(ProdRegion region, MainType mainType) {
        if (region == null) {
            return productRepository.findByMainType(mainType)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        } else {
            return productRepository.findByProdRegionAndMainType(region, mainType)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }
    }

    public ProductDTO getProductById(Long prodId) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + prodId));

        // Product를 ProductDTO로 변환하여 리턴
        return convertToDTO(product);
    }

    public List<ProductDTO> getProductsByFilters(LocalDate date, ProdRegion region, MainType mainType, SubType subType, String fishType) {
        Specification<Product> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();  // 기본 조건을 만들고

            if (date != null) {
                predicate = cb.and(predicate, cb.equal(root.get("createdAt"), date));  // createdAt 조건
            }
            if (region != null) {
                predicate = cb.and(predicate, cb.equal(root.get("prodRegion"), region));  // prodRegion 조건
            }
            if (mainType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("mainType"), mainType));  // mainType 조건
            }
            if (subType != null) {
                predicate = cb.and(predicate, cb.equal(root.get("subType"), subType));  // subType 조건
            }
            if (fishType != null && !fishType.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("prodName"), "%" + fishType + "%"));  // fishType 조건
            }

            return predicate;  // 모든 조건을 결합한 Predicate 반환
        };

        List<Product> products = productRepository.findAll(spec);  // 해당 조건에 맞는 결과를 찾음
        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }



    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdId(product.getProdId());
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion());
        dto.setMainType(product.getMainType());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        // LocalDateTime -> LocalDate 변환
        if (product.getCreatedAt() != null) {
            dto.setCreatedAt(product.getCreatedAt().toLocalDate());  // LocalDateTime에서 LocalDate만 추출
        }

        return dto;
    }


}
