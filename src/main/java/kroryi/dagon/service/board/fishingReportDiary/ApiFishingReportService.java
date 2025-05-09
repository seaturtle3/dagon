package kroryi.dagon.service.board.fishingReportDiary;

import kroryi.dagon.DTO.board.FishingReportDiary.ApiCommentDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiProductDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiUserDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiFishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;

    public ApiFishingReportDTO createFishingReport(ApiFishingReportDTO apiFishingReportDTO) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(apiFishingReportDTO.getTitle());
        fishingReport.setContent(apiFishingReportDTO.getContent());

        // Product 객체 설정
        Product product = new Product();
        if (apiFishingReportDTO.getProduct() != null) {
            product.setProdId(apiFishingReportDTO.getProduct().getProdId());
            product.setProdName(apiFishingReportDTO.getProduct().getProdName());
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

    public List<ApiFishingReportDTO> getAllFishingReports() {
        List<FishingReport> apiFishingReports = fishingReportRepository.findAllWithComments();
        return apiFishingReports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FishingReport getFishingReportById(@PathVariable Long fdId) {
        return fishingReportRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));
    }

    public Long updateFishingReport(Long fdId, ApiFishingReportDTO apiFishingReportDTO) {
        FishingReport fishingReport = fishingReportRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingReport.setTitle(apiFishingReportDTO.getTitle());
        fishingReport.setContent(apiFishingReportDTO.getContent());

        // User 객체 설정
        User user = userRepository.findById(apiFishingReportDTO.getUser().getUno())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingReport.setUser(user);

        // Product 객체 설정
        Product product = new Product();
        product.setProdId(apiFishingReportDTO.getProduct().getProdId());
        fishingReport.setProduct(product);

        fishingReportRepository.save(fishingReport);

        return fishingReport.getFrId();
    }

    public void deleteFishingReport(Long fdId) {
        fishingReportRepository.deleteById(fdId);
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
