package kroryi.dagon.service.product;

import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.ProductOption;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PartnerService partnerService;


    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;

    @Transactional
    public Long addProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);

        if (product.getPartner() == null) {
            Partner defaultPartner = partnerService.getDefaultPartner();
            product.setPartner(defaultPartner);
        }

        Product savedProduct = productRepository.save(product);
        return savedProduct.getProdId();
    }



    // [Read] 전체 상품 조회
    public Page<ProductDTO> getAllProductsApi (Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDTO::fromEntity);  // 생성자 대신 정적 메서드 사용
    }

    public Page<ProductDTO> getProductList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductDTO::fromEntity);
    }


    // [Read] id로 단건 조회
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id));
        return convertToDTO(product);
    }

    // [Update] 상품 수정
    @Transactional
    public Long updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id));

        product.setProdName(productDTO.getProdName());
        product.setProdRegion(productDTO.getProdRegion());
        product.setMainType(productDTO.getMainType());
        product.setSubType(productDTO.getSubType());
        product.setMaxPerson(productDTO.getMaxPerson());
        product.setMinPerson(productDTO.getMinPerson());
        product.setWeight(productDTO.getWeight());
        product.setProdAddress(productDTO.getProdAddress());
        product.setProdDescription(productDTO.getProdDescription());
        product.setProdEvent(productDTO.getProdEvent());
        product.setProdNotice(productDTO.getProdNotice());
        product.setProdThumbnail(productDTO.getProdThumbnail());

        return product.getProdId();
    }

    // [Delete] 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id));

        // product 안에 옵션들 돌면서 예약 체크
        for (ProductOption option : product.getOptions()) {
            if (seaFreshwaterFishingRepository.existsByProductOption_OptId(option.getOptId())) {
                throw new IllegalStateException("예약된 상품은 삭제할 수 없습니다.");
            }
        }

        productRepository.delete(product);
    }

    // Entity -> DTO 변환
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
        dto.setCreatedAt(product.getCreatedDate());
        dto.setProdThumbnail(product.getProdThumbnail());
        return dto;
    }

    // DTO -> Entity 변환
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProdName(dto.getProdName());
        product.setProdRegion(dto.getProdRegion());
        product.setMainType(dto.getMainType());
        product.setSubType(dto.getSubType());
        product.setMaxPerson(dto.getMaxPerson());
        product.setMinPerson(dto.getMinPerson());
        product.setWeight(dto.getWeight());
        product.setProdAddress(dto.getProdAddress());
        product.setProdDescription(dto.getProdDescription());
        product.setProdEvent(dto.getProdEvent());
        product.setProdNotice(dto.getProdNotice());
        product.setProdThumbnail(dto.getProdThumbnail());

        return product;
    }

    public void saveProduct(Product product) {

        if (product.getPartner() == null) {
            Partner defaultPartner = partnerService.getDefaultPartner();  // 기본 파트너 가져오기
            product.setPartner(defaultPartner);  // 파트너 자동 설정
        }
        productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 배가 없습니다. id=" + id));
    }

// ------------------------------------------------------------------------------------

    // 파트너 uno로 상품 리스트 조회
    public List<ProductDTO> getProductsByPartnerUno(String uno) {
        return productRepository.findByPartner_Uno(Long.valueOf(uno)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }




    public List<ProductDTO> getProductsByPartnerUno(Long partnerUno) {
        List<Product> products = productRepository.findByPartner_UnoAndDeletedFalse(partnerUno);
        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
    public void updateProducts(Long prodId, ProductDTO dto) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // ProductDTO에 있는 모든 필드를 제품에 반영 (필요에 따라 null 체크 추가 가능)
        product.setProdName(dto.getProdName());
        product.setProdRegion(dto.getProdRegion());
        product.setMainType(dto.getMainType());
        product.setSubType(dto.getSubType());
        product.setMaxPerson(dto.getMaxPerson());
        product.setMinPerson(dto.getMinPerson());
        product.setWeight(dto.getWeight());
        product.setProdAddress(dto.getProdAddress());
        product.setProdDescription(dto.getProdDescription());
        product.setProdEvent(dto.getProdEvent());
        product.setProdNotice(dto.getProdNotice());
        product.setProdThumbnail(dto.getProdThumbnail());
        // 필요하다면 prodPrice 등 가격 필드도 추가하세요

        productRepository.save(product);
    }



    public Product getProductEntityById(Long prodId) throws ChangeSetPersister.NotFoundException {
        return productRepository.findById(prodId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public void deleteProducts(Long prodId) throws ChangeSetPersister.NotFoundException {
        Product product = productRepository.findById(prodId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        product.setDeleted(true);
        productRepository.save(product);
    }


    @Transactional
    public void createProduct(ProductDTO dto, Long uno) {
        User partner = userRepository.findById(uno)
                .orElseThrow(() -> new RuntimeException("파트너를 찾을 수 없습니다."));

        Product product = new Product();
        product.setProdName(dto.getProdName());
        product.setProdRegion(dto.getProdRegion());
        product.setMainType(dto.getMainType());
        product.setSubType(dto.getSubType());
        product.setMaxPerson(dto.getMaxPerson());
        product.setMinPerson(dto.getMinPerson());
        product.setWeight(dto.getWeight());
        product.setProdAddress(dto.getProdAddress());
        product.setProdDescription(dto.getProdDescription());
        product.setProdEvent(dto.getProdEvent());
        product.setProdNotice(dto.getProdNotice());
        product.setProdThumbnail(dto.getProdThumbnail());

        // 파트너 (uno 외래키) 연결
        product.setPartner(partner.getPartner());

        productRepository.save(product);
    }


}
