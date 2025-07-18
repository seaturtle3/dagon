package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kroryi.dagon.entity.fishingCenter.FishingDiary;
import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiFishingDiaryDTO {
    private Long fdId;
    private String title;
    private String content;
    private String prodName;
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

    // 기존 이미지 유지 플래그 추가
    private Boolean keepExistingImages;

    // 기존 이미지 URL 리스트 (수정 시 기존 이미지 정보 전달용)
    private List<String> existingImageUrls;

    private String imageFileName;

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

            // 대표 썸네일 추출 - 우선순위: imageData > thumbnailData > imageUrl
            Optional<FishingDiaryImage> thumbnailImage = fishingDiary.getImages().stream()
                    .filter(FishingDiaryImage::isThumbnail)
                    .findFirst();

            if (thumbnailImage.isPresent()) {
                FishingDiaryImage image = thumbnailImage.get();
                if (image.getImageData() != null) {
                    this.thumbnailUrl = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(image.getImageData());
                } else if (image.getThumbnailData() != null) {
                    this.thumbnailUrl = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(image.getThumbnailData());
                } else {
                    this.thumbnailUrl = image.getImageUrl();
                }
                this.imageFileName = this.thumbnailUrl;
            }
        }
    }

    public static ApiFishingDiaryDTO fromEntity(FishingDiary fishingDiary) {
        return new ApiFishingDiaryDTO(fishingDiary);
    }

    // 홈페이지용 생성자 (이미지 데이터 제외)
    public static ApiFishingDiaryDTO fromEntityForHome(FishingDiary fishingDiary) {
        ApiFishingDiaryDTO dto = new ApiFishingDiaryDTO();
        dto.fdId = fishingDiary.getFdId();
        dto.title = fishingDiary.getTitle();
        dto.content = fishingDiary.getContent();
        dto.fishingAt = LocalDate.from(fishingDiary.getFishingAt());
        dto.createdAt = fishingDiary.getCreatedAt();

        if (fishingDiary.getProduct() != null) {
            dto.product = new ApiProductDTO(fishingDiary.getProduct());
            dto.prodName = fishingDiary.getProduct().getProdName();
        }

        if (fishingDiary.getUser() != null) {
            dto.user = new ApiUserDTO(fishingDiary.getUser());
        }

        // 이미지 처리 (Base64 데이터 우선)
        if (fishingDiary.getImages() != null) {
            dto.images = fishingDiary.getImages().stream()
                    .map(ApiFishingDiaryImageDTO::new) // Base64 데이터 포함
                    .collect(Collectors.toList());

            // 대표 썸네일 추출 - 우선순위: imageData > thumbnailData > imageUrl
            Optional<FishingDiaryImage> thumbnailImage = fishingDiary.getImages().stream()
                    .filter(FishingDiaryImage::isThumbnail)
                    .findFirst();

            if (thumbnailImage.isPresent()) {
                FishingDiaryImage image = thumbnailImage.get();
                if (image.getImageData() != null) {
                    dto.thumbnailUrl = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(image.getImageData());
                } else if (image.getThumbnailData() != null) {
                    dto.thumbnailUrl = "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(image.getThumbnailData());
                } else {
                    dto.thumbnailUrl = image.getImageUrl();
                }
                dto.imageFileName = dto.thumbnailUrl;
            }
        }

        return dto;
    }

}