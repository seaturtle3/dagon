package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.DTO.board.FishingCenter.ApiCommentDTO;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingCenter.ApiProductDTO;
import kroryi.dagon.DTO.board.FishingCenter.ApiUserDTO;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportImageRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiFishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FileStorageUtil fileStorageUtil;
    private final FishingReportImageRepository fishingReportImageRepository;

    public void saveImages(FishingReport fishingReport, List<MultipartFile> images) {
        List<FishingReportImage> imageEntities = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);

            // 이미지 저장 → URL 리턴
            String imageUrl = fileStorageUtil.saveImage(image, "fishing-report");

            // DB용 이미지 엔티티 생성
            FishingReportImage reportImage = new FishingReportImage();
            reportImage.setImageUrl(imageUrl);
            reportImage.setFishingReport(fishingReport); // 연관관계 주입
            reportImage.setThumbnail(i == 0); // 첫 번째 이미지를 썸네일로 지정

            imageEntities.add(reportImage);
        }

        fishingReportImageRepository.saveAll(imageEntities);
        fishingReport.setImages(imageEntities); // 양방향 매핑일 경우
    }

    @Transactional
    public ApiFishingReportDTO createFishingReport(ApiFishingReportDTO dto, Long userUno, List<MultipartFile> images) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(dto.getTitle());
        fishingReport.setContent(dto.getContent());
        fishingReport.setFishingAt(dto.getFishingAt());

        // 상품 설정
        if (dto.getProduct() != null) {
            Long prodId = dto.getProduct().getProdId();
            Product product = productRepository.findById(prodId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            fishingReport.setProduct(product);
        }

        // 사용자 설정
        User user = userRepository.findById(userUno)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        fishingReport.setUser(user);

        // 먼저 조황정보 저장 (PK 필요)
        fishingReport = fishingReportRepository.save(fishingReport);

        if (images != null && !images.isEmpty()) {
            saveImages(fishingReport, images);
        }

        return new ApiFishingReportDTO(fishingReport);
    }


    public Page<ApiFishingReportDTO> getAllFishingReports(Pageable pageable) {
        Page<FishingReport> fishingReports = fishingReportRepository.findAll(pageable);
        return fishingReports.map(ApiFishingReportDTO::new);  // 생성자 직접 호출
    }

    public ApiFishingReportDTO getFishingReportById(Long id) {
        FishingReport entity = fishingReportRepository.findById(id)
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


    public Long updateFishingReport(Long fdId, ApiFishingReportDTO apiFishingReportDTO) {
        FishingReport fishingReport = fishingReportRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingReport.setTitle(apiFishingReportDTO.getTitle());
        fishingReport.setContent(apiFishingReportDTO.getContent());
        fishingReport.setFishingAt(apiFishingReportDTO.getFishingAt());

        // User 객체 설정
        User user = userRepository.findById(apiFishingReportDTO.getUser().getUno())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingReport.setUser(user);

        if (apiFishingReportDTO.getProduct() != null) {
            Long prodId = apiFishingReportDTO.getProduct().getProdId();
            Product product = productRepository.getReferenceById(prodId);  // 영속성 컨텍스트에서 참조
            fishingReport.setProduct(product);
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
