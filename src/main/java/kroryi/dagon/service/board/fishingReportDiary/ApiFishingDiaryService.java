package kroryi.dagon.service.board.fishingReportDiary;

import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingDiaryDTO;
import kroryi.dagon.entity.FishingDiary;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiFishingDiaryService {

    private final FishingDiaryRepository fishingDiaryRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    // ProdId 찾기
    public List<ApiFishingDiaryDTO> getFishingDiariesByProdId(Long prodId) {
        List<FishingDiary> diaries = fishingDiaryRepository.findByProductProdId(prodId);
        return diaries.stream()
                .map(ApiFishingDiaryDTO::new)
                .collect(Collectors.toList());
    }

    public ApiFishingDiaryDTO createFishingDiary(ApiFishingDiaryDTO apiFishingDiaryDTO) {
        FishingDiary fishingDiary = new FishingDiary();
        fishingDiary.setTitle(apiFishingDiaryDTO.getTitle());
        fishingDiary.setContent(apiFishingDiaryDTO.getContent());
        fishingDiary.setThumbnailUrl(apiFishingDiaryDTO.getThumbnailUrl());
        fishingDiary.setFishingAt(apiFishingDiaryDTO.getFishingAt());

        fishingDiary.setProduct(apiFishingDiaryDTO.getProduct());
//        fishingDiary.setComments(fishingDiaryDTO.getComments());

        // 여기서 product 객체 찾아서 주입
        Long prodId = fishingDiaryDTO.getProduct().getProdId();
        Product product = productService.findById(prodId);  // <- 이거 중요
        fishingDiary.setProduct(product);

        // 임시 고정된 user
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        fishingDiary.setUser(user);

        fishingDiary = fishingDiaryRepository.save(fishingDiary);
        return new ApiFishingDiaryDTO(fishingDiary);
    }

    public List<ApiFishingDiaryDTO> getAllFishingDiary() {
        List<FishingDiary> fishingReports = fishingDiaryRepository.findAllWithComments();
        return fishingReports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FishingDiary getFishingDiary(@PathVariable Long id) {
        return fishingDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));
    }

    public Long updateFishingDiary(Long fdId, ApiFishingDiaryDTO apiFishingDiaryDTO) {
        FishingDiary fishingDiary = fishingDiaryRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingDiary.setTitle(apiFishingDiaryDTO.getTitle());
        fishingDiary.setContent(apiFishingDiaryDTO.getContent());
        fishingDiary.setFishingAt(apiFishingDiaryDTO.getFishingAt());

        fishingDiary.setUser(apiFishingDiaryDTO.getUser());
        fishingDiary.setProduct(apiFishingDiaryDTO.getProduct());
//        fishingDiary.setComments(apiFishingDiaryDTO.getComments());

        fishingDiaryRepository.save(fishingDiary);

        return fishingDiary.getFdId();
    }

    public void deleteFishingDiary(Long fdId) {
        fishingDiaryRepository.deleteById(fdId);
    }


    public FishingDiaryDTO convertToDTO(FishingDiary fishingdiary) {
        FishingDiaryDTO dto = new FishingDiaryDTO();
        dto.setFdId(fishingdiary.getFdId());
        dto.setTitle(fishingdiary.getTitle());
        dto.setContent(fishingdiary.getContent());
        dto.setThumbnailUrl(fishingdiary.getThumbnailUrl());
        dto.setFishingAt(fishingdiary.getFishingAt());
        dto.setModifyAt(fishingdiary.getModifyAt());
        dto.setViews(fishingdiary.getViews());
        dto.setUser(fishingdiary.getUser());
        dto.setProduct(fishingdiary.getProduct());
//        dto.setComments(fishingdiary.getComments());

        return dto;
    }

}
