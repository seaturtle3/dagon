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
import kroryi.dagon.repository.product.ProductImageRepository;
import kroryi.dagon.repository.product.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.PartnerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
        // 파트너 설정: DTO에 partnerUno가 있으면 사용, 없으면 기본 파트너 사용
        Partner partner;
        if (dto.getPartnerUno() != null) {
            partner = partnerRepository.findById(dto.getPartnerUno())
                    .orElseThrow(() -> new IllegalArgumentException("파트너를 찾을 수 없습니다. uno=" + dto.getPartnerUno()));
        } else {
            partner = partnerRepository.findById(uno).orElseThrow();
        }

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
        
        log.info("🔍 제품 저장 완료 - prodId: {}", product.getProdId());
        log.info("🔍 받은 이미지 개수: {}", productImages != null ? productImages.size() : 0);

        String firstImageUrl = null; // 첫 번째 이미지 URL 저장용

        if (productImages != null && !productImages.isEmpty()) {
            log.info("🔍 이미지 처리 시작");
            for (int i = 0; i < productImages.size(); i++) {
                MultipartFile file = productImages.get(i);
                log.info("🔍 처리 중인 이미지 {}: {}", i, file.getOriginalFilename());
                try {
                    // 1. 파일을 uploads 경로에 저장
                    String savedUrl = fileStorageUtil.saveImage(file, "products");
                    log.info("🔍 savedUrl: {}", savedUrl);
                    
                    // 첫 번째 이미지 URL 저장
                    if (i == 0) {
                        firstImageUrl = savedUrl;
                        log.info("🔍 첫 번째 이미지 URL 저장: {}", firstImageUrl);
                    }
                    
                    // 2. 저장된 파일을 읽어서 바이너리 추출
                    String uploadDir = fileStorageUtil.getUploadDir();
                    String relativePath = savedUrl.replaceFirst("/uploads/", "").replace("/", File.separator);
                    File savedFile = new File(uploadDir, relativePath);
                    log.info("🔍 savedFile: {}", savedFile);
                    byte[] imageBytes;
                    try (FileInputStream fis = new FileInputStream(savedFile)) {
                        imageBytes = StreamUtils.copyToByteArray(fis);
                    }
                    log.info("🔍 이미지 바이너리 크기: {} bytes", imageBytes.length);
                    
                    // 3. ProductImage 엔티티 생성 및 저장
                    ProductImage image = new ProductImage();
                    image.setFileName(savedUrl);
                    image.setProduct(product);
                    image.setImageData(imageBytes); // 바이너리 저장
                    productImageRepository.save(image);
                    log.info("🔍 ProductImage 저장 완료 - ID: {}", image.getId());
                } catch (Exception e) {
                    log.error("🔍 이미지 저장 실패: {}", e.getMessage(), e);
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
            
            // 첫 번째 이미지를 썸네일로 설정
            if (firstImageUrl != null) {
                product.setProdThumbnail(firstImageUrl);
                productRepository.save(product);
                log.info("🔍 썸네일 설정 완료: {}", firstImageUrl);
            }
        } else {
            log.info("🔍 이미지가 없거나 비어있음");
        }
        
        // 최종 확인
        Product savedProduct = productRepository.findById(product.getProdId()).orElse(null);
        if (savedProduct != null) {
            log.info("🔍 최종 제품 정보 - 썸네일: {}, 이미지 개수: {}", 
                    savedProduct.getProdThumbnail(), 
                    savedProduct.getImages().size());
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

    @Value("${file.upload-path}")
    private String uploadPath;

    // [Update] 상품 수정
    @Transactional
    public Long updateProduct(Long id, ProductDTO productDTO, List<MultipartFile> thumbnailFiles) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id));

        // 값 업데이트
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
        
        // 파트너 변경 처리
        if (productDTO.getPartnerUno() != null) {
            Partner newPartner = partnerRepository.findById(productDTO.getPartnerUno())
                    .orElseThrow(() -> new IllegalArgumentException("파트너를 찾을 수 없습니다. uno=" + productDTO.getPartnerUno()));
            product.setPartner(newPartner);
        }

        log.info("🧹 삭제 대상 이미지들1: {}", productDTO.getDeleteImageNames());

        

        // ✅ 삭제할 이미지 처리
        if (productDTO.getDeleteImageNames() != null) {
            log.info("🧹 삭제 대상 이미지들2: {}", productDTO.getDeleteImageNames());
            for (String imagePath : productDTO.getDeleteImageNames()) {
                fileStorageUtil.deleteImage(imagePath); // 파일 삭제
                log.info("🧹 삭제 대상 이미지들3: {}", imagePath);
                productImageRepository.deleteByProductAndFileName(product, imagePath); // DB 삭제
            }
        }

        // ✅ 새로 업로드된 이미지 저장
        if (thumbnailFiles != null && !thumbnailFiles.isEmpty()) {
            for (MultipartFile file : thumbnailFiles) {
                try {
                    String savedPath = fileStorageUtil.saveImage(file, "products"); // 파일 저장
                    // 저장된 파일을 읽어서 바이너리 추출
                    String uploadDir = fileStorageUtil.getUploadDir();
                    String relativePath = savedPath.replaceFirst("/uploads/", "").replace("/", java.io.File.separator);
                    File savedFile = new File(uploadDir, relativePath);
                    byte[] imageBytes;
                    try (FileInputStream fis = new FileInputStream(savedFile)) {
                        imageBytes = org.springframework.util.StreamUtils.copyToByteArray(fis);
                    }
                    ProductImage image = new ProductImage();
                    image.setFileName(savedPath);
                    image.setProduct(product);
                    image.setImageData(imageBytes);
                    productImageRepository.save(image);
                } catch (Exception e) {
                    throw new RuntimeException("상품 이미지 저장 실패", e);
                }
            }
        }

        log.info("🧹 삭제 대상 이미지들3: {}", productDTO.getDeleteImageNames());

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

    //  -------------- 프론트 api (바다/민물 상품 필터) ----------------
    public Page<ProductDTO> getProductsByMainType(MainType mainType, Pageable pageable) {
        Page<Product> products = productRepository.findByMainTypeAndDeletedFalse(mainType, pageable);
        return products.map(ProductDTO::fromEntity);
    }

    //  -------------- 프론트 api 바다 (지역, 상세장소, 어종 상품 필터 조회) ----------------
    public Page<ProductDTO> getFishingSeaProductsByFilters(ProdRegion region, SubType subType, List<String> species, Pageable pageable) {
        // species가 null이거나 빈 리스트인 경우 null로 처리
        List<String> safeSpecies = (species == null || species.isEmpty()) ? null : species;
        
        Page<Product> products = productRepository.findSeaProductsByFilters(
            region, 
            subType, 
            safeSpecies, 
            pageable
        );
        
        return products.map(product -> {
            ProductDTO dto = ProductDTO.fromEntity(product);
            List<String> fishSpeciesNames = product.getFishSpeciesMappings().stream()
                    .map(mapping -> mapping.getFs().getFsName())
                    .toList();
            dto.setFishSpeciesNames(fishSpeciesNames);
            return dto;
        });
    }

    //  -------------- 프론트 api 민물 (지역, 상세장소, 어종 상품 필터 조회) ----------------
    public Page<ProductDTO> getFishingFreshwaterProductsByFilters(ProdRegion region, SubType subType, List<String> species, Pageable pageable) {
        // species가 null이거나 빈 리스트인 경우 null로 처리
        List<String> safeSpecies = (species == null || species.isEmpty()) ? null : species;
        
        Page<Product> products = productRepository.findFreshwaterProductsByFilters(
            region, 
            subType, 
            safeSpecies, 
            pageable
        );
        
        return products.map(product -> {
            ProductDTO dto = ProductDTO.fromEntity(product);
            List<String> fishSpeciesNames = product.getFishSpeciesMappings().stream()
                    .map(mapping -> mapping.getFs().getFsName())
                    .toList();
            dto.setFishSpeciesNames(fishSpeciesNames);
            return dto;
        });
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

    // 관리자용 메서드들
    public Page<ProductDTO> getProductsByRegion(ProdRegion region, Pageable pageable) {
        Page<Product> products = productRepository.findByProdRegionAndDeletedFalse(region, pageable);
        return products.map(ProductDTO::fromEntity);
    }

    public Page<ProductDTO> getProductsByDeletedStatus(Boolean deleted, Pageable pageable) {
        Page<Product> products = productRepository.findByDeleted(deleted, pageable);
        return products.map(ProductDTO::fromEntity);
    }

    public long getTotalProductCount() {
        return productRepository.count();
    }

    public long getActiveProductCount() {
        return productRepository.countByDeletedFalse();
    }

    public long getDeletedProductCount() {
        return productRepository.countByDeletedTrue();
    }

    public long getProductCountByMainType(MainType mainType) {
        return productRepository.countByMainTypeAndDeletedFalse(mainType);
    }

}
