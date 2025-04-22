package kroryi.dagon.service;

import jakarta.persistence.criteria.Predicate;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<ProductDTO> getProductsByFilters(LocalDate date, MainType mainType, SubType subType, ProdRegion region, String fishType) {
        return productRepository.findAll((root, query, builder) -> {
                    Predicate predicate = builder.conjunction();  // 기본 조건을 결합

                    // 날짜 필터링: 등록된 날짜가 주어진 날짜 이후인 것만 가져오기
                    if (date != null) {
                        predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("createdAt"), date));
                    }

                    // MainType 필터링
                    if (mainType != null) {
                        predicate = builder.and(predicate, builder.equal(root.get("mainType"), mainType));
                    }

                    // SubType 필터링: mainType에 맞는 subType만 필터링
                    if (subType != null) {
                        predicate = builder.and(predicate, builder.equal(root.get("subType"), subType));
                    }

                    // 지역(ProdRegion) 필터링
                    if (region != null) {
                        predicate = builder.and(predicate, builder.equal(root.get("prodRegion"), region));
                    }

                    // 어종(fishType) 필터링: prodName에 대한 LIKE 검색
                    if (fishType != null && !fishType.isEmpty()) {
                        predicate = builder.and(predicate, builder.like(root.get("prodName"), "%" + fishType + "%"));
                    }

                    return predicate;
                }).stream()
                .map(this::convertToDTO)  // Product를 DTO로 변환
                .collect(Collectors.toList());  // 리스트로 반환
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
