package kroryi.dagon.service.community.fishingReportDiary;

import kroryi.dagon.DTO.board.FishingReportDiary.ApiCommentDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiProductDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiUserDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiFishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ApiFishingReportDTO createFishingReport(ApiFishingReportDTO apiFishingReportDTO, Long userUno) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(apiFishingReportDTO.getTitle());
        fishingReport.setContent(apiFishingReportDTO.getContent());
        fishingReport.setFishingAt(apiFishingReportDTO.getFishingAt());

        // 상품 설정
        if (apiFishingReportDTO.getProduct() != null) {
            Long prodId = apiFishingReportDTO.getProduct().getProdId();
            Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            fishingReport.setProduct(product);
        }

        // 사용자 설정
        User user = userRepository.findById(userUno)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        fishingReport.setUser(user);

        fishingReport = fishingReportRepository.save(fishingReport);
        return new ApiFishingReportDTO(fishingReport);
    }

    public Page<ApiFishingReportDTO> getAllFishingReports(Pageable pageable) {
        Page<FishingReport> fishingReports = fishingReportRepository.findAll(pageable);
        return fishingReports.map(this::convertToDTO); // map으로 DTO 변환
    }

    public ApiFishingReportDTO getFishingReportById(Long id) {
        FishingReport entity = fishingReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));
        return convertToDTO(entity);
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


    public ApiFishingReportDTO convertToDTO(FishingReport fishingReport) {
        ApiFishingReportDTO dto = new ApiFishingReportDTO();
        dto.setFrId(fishingReport.getFrId());
        dto.setTitle(fishingReport.getTitle());
        dto.setContent(fishingReport.getContent());

        ApiUserDTO userDTO = new ApiUserDTO(fishingReport.getUser());
        dto.setUser(userDTO);

        // Product 객체 변환
        ApiProductDTO productDTO = new ApiProductDTO(fishingReport.getProduct());
        dto.setProduct(productDTO);

        List<ApiCommentDTO> commentDTOs = fishingReport.getComments().stream()
                .map(ApiCommentDTO::new)
                .collect(Collectors.toList());
        dto.setComments(commentDTOs);

        return dto;
    }

}
