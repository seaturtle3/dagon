package kroryi.dagon.service.board.fishingReportDiary;

import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingDiaryDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.entity.FishingDiary;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ModelMapper modelMapper;

    // ProdId 찾기
    public Page<FishingDiaryDTO> getFishingDiariesByProdId(Long prodId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fishingAt").descending());
        Page<FishingDiary> diaryPage = fishingDiaryRepository.findByProductProdId(prodId, pageable);

        return diaryPage.map(this::convertToDTO);
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

    public FishingDiary findById(Long id) {
        return fishingDiaryRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("FishingDiary not found with id: " + id));
    }

    public FishingDiaryDTO findDTOById(Long id) {
        FishingDiary entity = fishingDiaryRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("조행기 X id: " + id));
        return modelMapper.map(entity, FishingDiaryDTO.class);
    }

    public FishingReportDTO updateFishingDiary(Long id,
                                                FishingDiaryDTO fishingDiaryDTO) {
        FishingDiary existing = fishingDiaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("조행기 X ID : " + id));

        existing.setTitle(fishingDiaryDTO.getTitle());
        existing.setContent(fishingDiaryDTO.getContent());
        existing.setThumbnailUrl(fishingDiaryDTO.getThumbnailUrl());
        existing.setFishingAt(fishingDiaryDTO.getFishingAt());

        fishingDiaryRepository.save(existing);

        return modelMapper.map(existing, FishingReportDTO.class);
    }

    public Long deleteAndReturnProdId(Long id) {
        FishingDiary diary = fishingDiaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 조행기 ID: " + id));

        Long prodId = diary.getProduct().getProdId(); // 삭제 후 리스트로 돌아가기 위해 prodId 보관
        fishingDiaryRepository.delete(diary);

        return prodId;
    }

    public FishingDiaryDTO convertToDTO(FishingDiary diary) {
        FishingDiaryDTO dto = new FishingDiaryDTO();
        dto.setFdId(diary.getFdId());
        dto.setTitle(diary.getTitle());
        dto.setViews(diary.getViews());
        dto.setProdName(diary.getProduct().getProdName());
        dto.setUserName(diary.getUser().getUname());
        dto.setFishingAt(diary.getFishingAt());
        dto.setThumbnailUrl(diary.getThumbnailUrl());
        return dto;
    }

}
