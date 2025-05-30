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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiFishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ApiFishingReportDTO createFishingReport(ApiFishingReportDTO apiFishingReportDTO) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(apiFishingReportDTO.getTitle());
        fishingReport.setContent(apiFishingReportDTO.getContent());
        fishingReport.setFishingAt(apiFishingReportDTO.getFishingAt());


        if (apiFishingReportDTO.getProduct() != null) {
            Long prodId = apiFishingReportDTO.getProduct().getProdId();
            Product product = productRepository.getReferenceById(prodId);
            fishingReport.setProduct(product);
        }

        // 실제 로그인 후 유저 ID, 예약 상품 ID
//        User user = userRepository.findById(fishingReportDTO.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 임시 고정된 user
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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
