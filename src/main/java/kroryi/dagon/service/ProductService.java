package kroryi.dagon.service;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PartnerService partnerService;

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion());
        dto.setMainType(product.getMainType());
        dto.setSubType(product.getSubType());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        return dto;
    }

    public void saveProduct(Product product) {

        if (product.getPartner() == null) {
            Partner defaultPartner = partnerService.getDefaultPartner();  // 기본 파트너 가져오기
            product.setPartner(defaultPartner);  // 파트너 자동 설정
        }

        productRepository.save(product);
    }



}
