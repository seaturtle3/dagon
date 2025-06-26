package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.DTO.board.FishingCenter.ApiFishingReportDTO;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import kroryi.dagon.repository.product.ProductRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportImageRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ApiFishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FileStorageUtil fileStorageUtil;
    private final FishingReportImageRepository fishingReportImageRepository;

    @Transactional
    public ApiFishingReportDTO createFishingReport(ApiFishingReportDTO dto, Long userUno, List<MultipartFile> images) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(dto.getTitle());
        fishingReport.setContent(dto.getContent());

        if (dto.getFishingAt() != null) {
            fishingReport.setFishingAt(dto.getFishingAt().atStartOfDay());
        }

        log.info("dto.product:---> {}", dto.getProduct());

        // 상품 설정
        if (dto.getProduct() != null && dto.getProduct().getProdId() != null) {
            Long prodId = dto.getProduct().getProdId();
            Product product = productRepository.findById(prodId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            fishingReport.setProduct(product);
        }

        // 사용자 설정
        User user = userRepository.findById(userUno)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        fishingReport.setUser(user);

        // 조황정보 저장 (PK 확보)
        fishingReport = fishingReportRepository.save(fishingReport);

        // 이미지 저장 처리
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                try {
                    // 1. 파일을 uploads 경로에 저장
                    String savedUrl = fileStorageUtil.saveImage(file, "fishing-report");
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
                    FishingReportImage image = new FishingReportImage();
                    image.setImageUrl(savedUrl); // 파일 경로 저장
                    image.setImageData(imageBytes); // 바이너리 저장
                    image.setThumbnail(i == 0); // 첫 번째 이미지를 썸네일로
                    image.setOrderIndex(i);
                    image.setFishingReport(fishingReport);
                    fishingReportImageRepository.save(image);
                } catch (Exception e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        }
        return new ApiFishingReportDTO(fishingReport);
    }

    public Page<ApiFishingReportDTO> getAllFishingReports(Pageable pageable) {
        Page<FishingReport> fishingReports = fishingReportRepository.findAll(pageable);
        return fishingReports.map(ApiFishingReportDTO::new);  // 생성자 직접 호출
    }

    public ApiFishingReportDTO getFishingReportById(Long id) {
        FishingReport entity = fishingReportRepository.findByIdWithComments(id)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));
        return new ApiFishingReportDTO(entity);  // 생성자 호출
    }

    // 특정 제품ID 조황정보 조회
    public List<ApiFishingReportDTO> getAllByProductId(Long productId) {
        List<FishingReport> reports = fishingReportRepository.findByProduct_ProdId(productId);

        if (reports.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }

        // 전체 리스트를 DTO로 변환
        return reports.stream()
                .map(ApiFishingReportDTO::fromEntity)
                .collect(Collectors.toList());
    }

//    ---------------------------ApiFishingCenterController-----------------------------------------
    public List<ApiFishingReportDTO> getAll() {
        return fishingReportRepository.findAll().stream()
                .map(ApiFishingReportDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateFishingReport(Long frId, ApiFishingReportDTO apiFishingReportDTO, List<MultipartFile> images) {
        FishingReport fishingReport = fishingReportRepository.findById(frId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingReport.setTitle(apiFishingReportDTO.getTitle());
        fishingReport.setContent(apiFishingReportDTO.getContent());
        
        if(apiFishingReportDTO.getFishingAt() != null) {
            fishingReport.setFishingAt(apiFishingReportDTO.getFishingAt().atStartOfDay());
        }

        // User 객체 설정
        User user = userRepository.findById(apiFishingReportDTO.getUser().getUno())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingReport.setUser(user);

        if (apiFishingReportDTO.getProduct() != null && apiFishingReportDTO.getProduct().getProdId() != null) {
            Long prodId = apiFishingReportDTO.getProduct().getProdId();
            Product product = productRepository.getReferenceById(prodId);  // 영속성 컨텍스트에서 참조
            fishingReport.setProduct(product);
        }

        // 기존 이미지 삭제
        fishingReportImageRepository.deleteAll(fishingReport.getImages());
        fishingReport.getImages().clear();

        // 새 이미지 저장
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                try {
                    FishingReportImage image = new FishingReportImage();
                    image.setImageData(file.getBytes());
                    image.setOrderIndex(i);
                    image.setFishingReport(fishingReport);
                    fishingReportImageRepository.save(image);
                    fishingReport.getImages().add(image);
                } catch (Exception e) {
                    throw new RuntimeException("이미지 저장 실패", e);
                }
            }
        }

        fishingReportRepository.save(fishingReport);
        return fishingReport.getFrId();
    }

    public void deleteFishingReport(Long id) {
        FishingReport fishingReport = fishingReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));
        fishingReportRepository.delete(fishingReport);
    }
}
