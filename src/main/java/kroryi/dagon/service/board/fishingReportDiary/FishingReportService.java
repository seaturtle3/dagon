package kroryi.dagon.service.board.fishingReportDiary;

import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;

    // ProdId 찾기
    public Page<FishingReportDTO> getFishingReportsByProdId(Long prodId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fishingAt").descending());
        Page<FishingReport> reportPage = fishingReportRepository.findByProductProdId(prodId, pageable);

        return reportPage.map(this::convertToDTO);
    }


    public FishingReportDTO createFishingReport(FishingReportDTO fishingReportDTO) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(fishingReportDTO.getTitle());
        fishingReport.setContent(fishingReportDTO.getContent());
        fishingReport.setThumbnailUrl(fishingReportDTO.getThumbnailUrl());
        fishingReport.setFishingAt(fishingReportDTO.getFishingAt());
        fishingReport.setModifyAt(fishingReportDTO.getModifyAt());
        fishingReport.setViews(fishingReportDTO.getViews());
        fishingReport.setProduct(fishingReportDTO.getProduct());
        fishingReport.setComments(fishingReportDTO.getComments());

        // 실제 로그인 후 유저 ID, 예약 상품 ID
//        User user = userRepository.findById(fishingReportDTO.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 임시 고정된 user
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingReport.setUser(user);

        fishingReport = fishingReportRepository.save(fishingReport);
        return new FishingReportDTO(fishingReport);
    }


    private FishingReportDTO convertToDTO(FishingReport report) {
        FishingReportDTO dto = new FishingReportDTO();
        dto.setFrId(report.getFrId());
        dto.setTitle(report.getTitle());
        dto.setViews(report.getViews());
        dto.setProdName(report.getProduct().getProdName());
        dto.setUserName(report.getUser().getUname());
        dto.setFishingAt(report.getFishingAt());
        dto.setThumbnailUrl(report.getThumbnailUrl());
        return dto;
    }


}
