package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kroryi.dagon.entity.fishingCenter.FishingDiary;
import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiFishingDiaryDTO {
    private Long fdId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fishingAt;

    private ApiProductDTO product;
    private ApiUserDTO user;
    private List<ApiCommentDTO> comments;

    // 이미지 DTO 리스트 추가
    private List<ApiFishingDiaryImageDTO> images;

    // 대표 썸네일도 따로 뽑아서 담기
    private String thumbnailUrl;

    public ApiFishingDiaryDTO(FishingDiary fishingDiary) {
        this.fdId = fishingDiary.getFdId();
        this.title = fishingDiary.getTitle();
        this.content = fishingDiary.getContent();
        this.fishingAt = LocalDate.from(fishingDiary.getFishingAt());
        this.createdAt = fishingDiary.getCreatedAt();

        if (fishingDiary.getProduct() != null) {
            this.product = new ApiProductDTO(fishingDiary.getProduct());
        }

        if (fishingDiary.getUser() != null) {
            this.user = new ApiUserDTO(fishingDiary.getUser());
        }

        if (fishingDiary.getComments() != null) {
            this.comments = fishingDiary.getComments().stream()
                    .map(ApiCommentDTO::new)
                    .collect(Collectors.toList());
        }

        // 이미지 리스트 매핑
        if (fishingDiary.getImages() != null) {
            this.images = fishingDiary.getImages().stream()
                    .map(ApiFishingDiaryImageDTO::new)
                    .collect(Collectors.toList());

            // 대표 썸네일 추출
            this.thumbnailUrl = fishingDiary.getImages().stream()
                    .filter(FishingDiaryImage::isThumbnail)
                    .map(FishingDiaryImage::getImageUrl)
                    .findFirst()
                    .orElse(null);
        }
    }

    public static ApiFishingDiaryDTO fromEntity(FishingDiary fishingDiary) {
        return new ApiFishingDiaryDTO(fishingDiary);
    }

}