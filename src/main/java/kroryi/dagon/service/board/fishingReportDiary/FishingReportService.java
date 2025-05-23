package kroryi.dagon.service.board.fishingReportDiary;

import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

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

    public FishingReport findById(Long id) {
        return fishingReportRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("FishingReport not found with id: " + id));
    }

    public FishingReportDTO findDTOById(Long id) {
        FishingReport entity = fishingReportRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("조황정보 X id: " + id));
        return modelMapper.map(entity, FishingReportDTO.class);
    }

    public FishingReportDTO updateFishingReport(Long id,
                                                FishingReportDTO fishingReportDTO) {
        FishingReport existing = fishingReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("조황정보 X ID : " + id));

        existing.setTitle(fishingReportDTO.getTitle());
        existing.setContent(fishingReportDTO.getContent());
        existing.setThumbnailUrl(fishingReportDTO.getThumbnailUrl());
        existing.setFishingAt(fishingReportDTO.getFishingAt());

        fishingReportRepository.save(existing);

        return modelMapper.map(existing, FishingReportDTO.class);
    }

    public Long deleteAndReturnProdId(Long id) {
        FishingReport report = fishingReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 조황 ID: " + id));

        Long prodId = report.getProduct().getProdId(); // 삭제 후 리스트로 돌아가기 위해 prodId 보관
        fishingReportRepository.delete(report);

        return prodId;
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
