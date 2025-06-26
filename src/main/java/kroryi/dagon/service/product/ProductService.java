package kroryi.dagon.service.product;

import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.product.ProductDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.product.ProductImage;
import kroryi.dagon.entity.product.ProductOption;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.PartnerRepository;
import kroryi.dagon.repository.product.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kroryi.dagon.util.FileStorageUtil;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import org.springframework.util.StreamUtils;
import lombok.extern.log4j.Log4j2;
import kroryi.dagon.repository.product.ProductImageRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerService partnerService;
    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;
    private final FileStorageUtil fileStorageUtil;
    @Transactional
    public void createProductWithImages(ProductDTO dto, Long uno, List<MultipartFile> productImages) {
        Partner partner = partnerRepository.findById(uno).orElseThrow();

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
        product.setPartner(partner);

        productRepository.save(product);

        if (productImages != null) {
            for (int i = 0; i < productImages.size(); i++) {
                MultipartFile file = productImages.get(i);
                try {
                    // 1. 파일을 uploads 경로에 저장
                    String savedUrl = fileStorageUtil.saveImage(file, "products");
                    log.info("savedUrl:---> {}", savedUrl);
                    // 2. 저장된 파일을 읽어서 바이너리 추출
                    String uploadDir = fileStorageUtil.getUploadDir();
                    String relativePath = savedUrl.replaceFirst("/uploads/", "").replace("/", File.separator);
                    File savedFile = new File(uploadDir, relativePath);
                    log.info("savedFile:---> {}", savedFile);
                    byte[] imageBytes;
                    try (FileInputStream fis = new FileInputStream(savedFile)) {
                        imageBytes = StreamUtils.copyToByteArray(fis);
                    }
                    // 3. FishingReportImage 엔티티 생성 및 저장
                    ProductImage image = new ProductImage();
                    image.setImageData(imageBytes); // 바이너리 저장
                    productImageRepository.save(image);
                } catch (Exception e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        } else if (dto.getProdImageNames() != null) {
            for (String fileName : dto.getProdImageNames()) {
                ProductImage image = new ProductImage();
                image.setFileName(fileName);
                image.setProduct(product);
                product.addImage(image);
            }
        }
    }

    // [Read] 전체 상품 조회
    public Page<ProductDTO> getAllProductsApi(Pageable pageable) {
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
        return ProductDTO.fromEntity(product);
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

    //  -------------- 프론트 api 추가(바다/민물 필터) ----------------
    public Page<ProductDTO> getProductsByMainType(MainType mainType, Pageable pageable) {
        Page<Product> products = productRepository.findByMainTypeAndDeletedFalse(mainType, pageable);
        return products.map(ProductDTO::fromEntity);
    }

    //  -------------- 프론트 api 추가(날짜, 지역, 상세 장소, 어종에 따라 바다 상품 필터 조회) ----------------
    public List<ProductDTO> getFishingCenterProductsByFilters(ProdRegion region, SubType subType, String species) {
        List<Product> products = productRepository.findSeaProductsByFilters(region, subType, species);

        return products.stream().map(product -> {
            ProductDTO dto = ProductDTO.fromEntity(product);

            // fishSpecies 이름 리스트 추가
            List<String> fishSpeciesNames = product.getFishSpeciesMappings().stream()
                    .map(mapping -> mapping.getFs().getFsName())
                    .toList();

            dto.setFishSpeciesNames(fishSpeciesNames);  // 이 필드를 DTO에 추가해야 함

            return dto;
        }).toList();
    }


// ------------------------------------------------------------------------------------

    // 파트너 uno로 상품 리스트 조회
    public List<ProductDTO> getProductsByPartnerUno(String uno) {
        return productRepository.findByPartner_Uno(Long.valueOf(uno)).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByPartnerUno(Long partnerUno) {
        List<Product> products = productRepository.findByPartner_Uno(partnerUno);
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

    @Transactional(readOnly = true)
    public Long getProductCountByPartnerId(Long partnerId) {
        return productRepository.countByPartner_UnoAndDeletedFalse(partnerId);
    }

    public Product restoreProduct(Long prodId) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (!product.isDeleted()) {
            throw new RuntimeException("이미 복구된 상품입니다.");
        }

        product.setDeleted(false);
        return productRepository.save(product);
    }

    // 키워드로 상품 검색 (페이징)
    public Page<ProductDTO> searchProductsByKeyword(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.searchByKeyword(keyword, pageable);
        return products.map(ProductDTO::fromEntity);
    }

}
