package kroryi.dagon.service.board.fishingReportDiary;

import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
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
public class FishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final UserRepository userRepository;

    // ProdId 찾기
    public List<FishingReportDTO> getFishingReportsByProdId(Long prodId) {
        List<FishingReport> reports = fishingReportRepository.findByProductProdId(prodId);
        return reports.stream()
                .map(FishingReportDTO::new)
                .collect(Collectors.toList());
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

}
