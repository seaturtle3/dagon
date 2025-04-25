package kroryi.dagon.service;

import kroryi.dagon.DTO.board.FishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.FishingDiary;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingDiaryService {

    private final FishingDiaryRepository fishingDiaryRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    // ProdId 찾기
    public List<FishingDiaryDTO> getFishingDiariesByProdId(Long prodId) {
        List<FishingDiary> diaries = fishingDiaryRepository.findByProductProdId(prodId);
        return diaries.stream()
                .map(FishingDiaryDTO::new)
                .collect(Collectors.toList());
    }

    public FishingDiaryDTO createFishingDiary(FishingDiaryDTO fishingDiaryDTO) {
        FishingDiary fishingDiary = new FishingDiary();
        fishingDiary.setTitle(fishingDiaryDTO.getTitle());
        fishingDiary.setContent(fishingDiaryDTO.getContent());
        fishingDiary.setThumbnailUrl(fishingDiaryDTO.getThumbnailUrl());
        fishingDiary.setFishingAt(fishingDiaryDTO.getFishingAt());
        fishingDiary.setModifyAt(fishingDiaryDTO.getModifyAt());
        fishingDiary.setViews(fishingDiaryDTO.getViews());
        fishingDiary.setProduct(fishingDiaryDTO.getProduct());
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
        return new FishingDiaryDTO(fishingDiary);
    }

    public List<FishingDiaryDTO> getAllFishingDiary() {
        List<FishingDiary> fishingReports = fishingDiaryRepository.findAllWithComments();
        return fishingReports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FishingDiary getFishingDiary(@PathVariable Long id) {
        return fishingDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));
    }

    public Long updateFishingDiary(Long fdId, FishingDiaryDTO fishingDiaryDTO) {
        FishingDiary fishingDiary = fishingDiaryRepository.findById(fdId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingDiary.setTitle(fishingDiaryDTO.getTitle());
        fishingDiary.setContent(fishingDiaryDTO.getContent());
        fishingDiary.setThumbnailUrl(fishingDiaryDTO.getThumbnailUrl());
        fishingDiary.setFishingAt(fishingDiaryDTO.getFishingAt());
        fishingDiary.setModifyAt(fishingDiaryDTO.getModifyAt());
        fishingDiary.setViews(fishingDiaryDTO.getViews());
        fishingDiary.setUser(fishingDiaryDTO.getUser());
        fishingDiary.setProduct(fishingDiaryDTO.getProduct());
//        fishingDiary.setComments(fishingDiaryDTO.getComments());

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
