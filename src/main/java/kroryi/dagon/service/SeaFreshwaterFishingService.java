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

    // 필터 : 메인타입, 서브타입, 지역
    public List<Product> getProductsByFilters(MainType mainType, SubType subType, ProdRegion region) {
        if (subType == null && region == null) {
            return productRepository.findByMainType(mainType);
        } else if (subType == null) {
            return productRepository.findByMainTypeAndProdRegion(mainType, region);
        } else if (region == null) {
            return productRepository.findByMainTypeAndSubType(mainType, subType);
        } else {
            return productRepository.findByMainTypeAndSubTypeAndProdRegion(mainType, subType, region);
        }
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
