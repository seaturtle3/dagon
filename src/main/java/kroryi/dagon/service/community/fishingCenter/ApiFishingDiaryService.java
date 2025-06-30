package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.DTO.board.FishingCenter.ApiFishingDiaryDTO;
import kroryi.dagon.entity.fishingCenter.FishingDiary;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingDiaryImageRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import kroryi.dagon.repository.product.ProductRepository;
import kroryi.dagon.service.product.ProductService;
import kroryi.dagon.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileInputStream;

@Service
@RequiredArgsConstructor
public class ApiFishingDiaryService {

    private final FishingDiaryRepository fishingDiaryRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final FileStorageUtil fileStorageUtil;
    private final FishingDiaryImageRepository fishingDiaryImageRepository;
    private final ProductRepository productRepository;

    // 이미지 저장
    public void saveImages(FishingDiary fishingDiary, List<MultipartFile> images) {
        List<FishingDiaryImage> imageEntities = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);

            // 이미지 저장 → URL 리턴
            String imageUrl = fileStorageUtil.saveImage(image, "fishing-diary");

            // DB용 이미지 엔티티 생성
            FishingDiaryImage diaryImage = new FishingDiaryImage();
            diaryImage.setImageUrl(imageUrl);
            diaryImage.setFishingDiary(fishingDiary); // 연관관계 주입
            diaryImage.setThumbnail(i == 0); // 첫 번째 이미지를 썸네일로 지정

            imageEntities.add(diaryImage);
        }

        fishingDiaryImageRepository.saveAll(imageEntities);
        fishingDiary.setImages(imageEntities); // 양방향 매핑일 경우
    }

    @Transactional
    public ApiFishingDiaryDTO createFishingDiary(ApiFishingDiaryDTO dto, Long userUno, List<MultipartFile> images) {
        FishingDiary fishingDiary = new FishingDiary();
        fishingDiary.setTitle(dto.getTitle());
        fishingDiary.setContent(dto.getContent());
        if (dto.getFishingAt() != null) {
            fishingDiary.setFishingAt(dto.getFishingAt().atStartOfDay());
        } else {
            fishingDiary.setFishingAt(java.time.LocalDate.now().atStartOfDay());
        }

        // 사용자 설정
        User user = userRepository.findById(userUno)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        fishingDiary.setUser(user);

        // 상품 설정
        if (dto.getProduct() != null && dto.getProduct().getProdId() != null) {
            Long prodId = dto.getProduct().getProdId();
            Product product = productRepository.findById(prodId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            fishingDiary.setProduct(product);
        }


        // 먼저 조행기 저장 (PK 필요)
        fishingDiary = fishingDiaryRepository.save(fishingDiary);

        // 이미지 저장
        if (images != null && !images.isEmpty()) {
            saveImages(fishingDiary, images);
        }

        return new ApiFishingDiaryDTO(fishingDiary);
    }

    public Page<ApiFishingDiaryDTO> getAllFishingDiary(Pageable pageable) {
        Page<FishingDiary> page = fishingDiaryRepository.findAll(pageable); // pageable 적용
        return page.map(ApiFishingDiaryDTO::new); // Page<Entity> → Page<DTO>
    }

    public ApiFishingDiaryDTO getFishingDiaryById(@PathVariable Long id) {
        FishingDiary fishingDiary = fishingDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));

        // FishingDiary를 ApiFishingDiaryDTO로 변환
        return new ApiFishingDiaryDTO(fishingDiary);
    }

    // 특정 제품ID 조행기 조회
    public List<ApiFishingDiaryDTO> getAllByProductId(Long productId) {
        List<FishingDiary> diaries = fishingDiaryRepository.findByProduct_ProdId(productId);

        if (diaries.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        // 전체 리스트를 DTO로 변환
        return diaries.stream()
                .map(ApiFishingDiaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

//    ---------------------------ApiFishingCenterController-----------------------------------------
    public List<ApiFishingDiaryDTO> getAll() {
        return fishingDiaryRepository.findAll().stream()
                .map(ApiFishingDiaryDTO::new)
                .collect(Collectors.toList());
    }

    public Long updateFishingDiary(Long fdId, ApiFishingDiaryDTO apiFishingDiaryDTO, List<MultipartFile> images) {
        FishingDiary fishingDiary = fishingDiaryRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingDiary.setTitle(apiFishingDiaryDTO.getTitle());
        fishingDiary.setContent(apiFishingDiaryDTO.getContent());
        if (apiFishingDiaryDTO.getFishingAt() != null) {
            fishingDiary.setFishingAt(apiFishingDiaryDTO.getFishingAt().atStartOfDay());
        } else {
            fishingDiary.setFishingAt(java.time.LocalDate.now().atStartOfDay());
        }

        // User 설정
        Long userId = apiFishingDiaryDTO.getUser().getUno();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingDiary.setUser(user);

        // Product 설정
        Long prodId = apiFishingDiaryDTO.getProduct().getProdId();
        Product product = productService.findById(prodId);
        fishingDiary.setProduct(product);

        // 기존 이미지 삭제
        fishingDiaryImageRepository.deleteAll(fishingDiary.getImages());
        fishingDiary.getImages().clear();

        // 새 이미지 저장
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                try {
                    // 원본 이미지 저장
                    String imageUrl = fileStorageUtil.saveImage(file, "fishing-diary");
                    byte[] imageBytes = file.getBytes();
                    // 썸네일 생성 및 저장 (FileStorageUtil 활용)
                    byte[] thumbnailBytes = null;
                    if (i == 0) {
                        String thumbUrl = fileStorageUtil.saveImageWithThumbnail(file, "fishing-diary");
                        // 썸네일 파일 읽기
                        String uploadDir = fileStorageUtil.getUploadDir();
                        String dateFolder = imageUrl.replaceFirst("/uploads/fishing-diary/", "").replaceAll("/[^/]+$", "");
                        String thumbFileName = "thumb_" + file.getOriginalFilename();
                        java.nio.file.Path thumbPath = java.nio.file.Paths.get(uploadDir, "fishing-diary", dateFolder, thumbFileName);
                        if (java.nio.file.Files.exists(thumbPath)) {
                            thumbnailBytes = java.nio.file.Files.readAllBytes(thumbPath);
                        }
                    }
                    FishingDiaryImage diaryImage = new FishingDiaryImage();
                    diaryImage.setImageUrl(imageUrl);
                    diaryImage.setImageData(imageBytes);
                    diaryImage.setThumbnail(i == 0);
                    diaryImage.setOrderIndex(i);
                    diaryImage.setFishingDiary(fishingDiary);
                    diaryImage.setThumbnailData(thumbnailBytes);
                    fishingDiaryImageRepository.save(diaryImage);
                    fishingDiary.getImages().add(diaryImage);
                } catch (Exception e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        }

        fishingDiaryRepository.save(fishingDiary);
        return fishingDiary.getFdId();
    }

    public Long updateFishingDiary(Long fdId, ApiFishingDiaryDTO apiFishingDiaryDTO, Long userUno, List<MultipartFile> images) {
        FishingDiary fishingDiary = fishingDiaryRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingDiary.setTitle(apiFishingDiaryDTO.getTitle());
        fishingDiary.setContent(apiFishingDiaryDTO.getContent());
        if (apiFishingDiaryDTO.getFishingAt() != null) {
            fishingDiary.setFishingAt(apiFishingDiaryDTO.getFishingAt().atStartOfDay());
        } else {
            fishingDiary.setFishingAt(java.time.LocalDate.now().atStartOfDay());
        }

        // User 설정 (컨트롤러에서 전달받은 userUno 사용)
        User user = userRepository.findById(userUno)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingDiary.setUser(user);

        // Product 설정
        if (apiFishingDiaryDTO.getProduct() != null && apiFishingDiaryDTO.getProduct().getProdId() != null) {
            Long prodId = apiFishingDiaryDTO.getProduct().getProdId();
            Product product = productService.findById(prodId);
            fishingDiary.setProduct(product);
        }

        // 기존 이미지 삭제
        fishingDiaryImageRepository.deleteAll(fishingDiary.getImages());
        fishingDiary.getImages().clear();

        // 새 이미지 저장
        // ✅ 새로 업로드된 이미지 저장
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                try {
                    String savedPath = fileStorageUtil.saveImage(file, "fishing-diary"); // 파일 저장
                    // 저장된 파일을 읽어서 바이너리 추출
                    String uploadDir = fileStorageUtil.getUploadDir();
                    String relativePath = savedPath.replaceFirst("/uploads/", "").replace("/", java.io.File.separator);
                    File savedFile = new File(uploadDir, relativePath);
                    byte[] imageBytes;
                    try (FileInputStream fis = new FileInputStream(savedFile)) {
                        imageBytes = org.springframework.util.StreamUtils.copyToByteArray(fis);
                    }
                    FishingDiaryImage image = new FishingDiaryImage();
                    image.setImageUrl(savedPath);
                    image.setImageData(imageBytes);
                    image.setFishingDiary(fishingDiary);
                    image.setThumbnail(false);
                    image.setOrderIndex(0);
                    fishingDiaryImageRepository.save(image);
                } catch (Exception e) {
                    throw new RuntimeException("상품 이미지 저장 실패", e);
                }
            }
        }

        fishingDiaryRepository.save(fishingDiary);
        return fishingDiary.getFdId();
    }

    public Long updateFishingDiary(Long fdId, ApiFishingDiaryDTO apiFishingDiaryDTO, Long userUno) {
        FishingDiary fishingDiary = fishingDiaryRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingDiary.setTitle(apiFishingDiaryDTO.getTitle());
        fishingDiary.setContent(apiFishingDiaryDTO.getContent());
        if (apiFishingDiaryDTO.getFishingAt() != null) {
            fishingDiary.setFishingAt(apiFishingDiaryDTO.getFishingAt().atStartOfDay());
        } else {
            fishingDiary.setFishingAt(java.time.LocalDate.now().atStartOfDay());
        }

        // User 설정 (컨트롤러에서 전달받은 userUno 사용)
        User user = userRepository.findById(userUno)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingDiary.setUser(user);

        // Product 설정
        if (apiFishingDiaryDTO.getProduct() != null && apiFishingDiaryDTO.getProduct().getProdId() != null) {
            Long prodId = apiFishingDiaryDTO.getProduct().getProdId();
            Product product = productService.findById(prodId);
            fishingDiary.setProduct(product);
        }

        fishingDiaryRepository.save(fishingDiary);
        return fishingDiary.getFdId();
    }

    public void deleteFishingDiary(Long fdId) {
        fishingDiaryRepository.deleteById(fdId);
    }

    public List<ApiFishingDiaryDTO> getMyDiaries(Long userUno) {
        List<FishingDiary> diaries = fishingDiaryRepository.findByUser_Uno(userUno);
        return diaries.stream()
                .map(ApiFishingDiaryDTO::new)
                .collect(Collectors.toList());
    }

    public List<ApiFishingDiaryDTO> getDiariesByMyProducts(Long userUno) {
        // 1. 유저가 등록한 상품들 조회
        List<Product> myProducts = productRepository.findByPartner_Uno(userUno);
        if (myProducts.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> productIds = myProducts.stream()
                .map(Product::getProdId)
                .collect(Collectors.toList());
        // 2. 상품 id 리스트로 조행기 조회
        List<FishingDiary> diaries = fishingDiaryRepository.findByProduct_ProdIdIn(productIds);
        // 3. DTO 변환
        return diaries.stream()
                .map(ApiFishingDiaryDTO::new)
                .collect(Collectors.toList());
    }

}
