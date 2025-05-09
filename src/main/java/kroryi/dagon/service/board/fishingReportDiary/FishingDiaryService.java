package kroryi.dagon.service.board.fishingReportDiary;

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

}
