package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiFishingReportDTO {
    private Long frId;
    private String title;
    private String content;
    private String prodName;
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fishingAt;

    private ApiProductDTO product;
    private ApiUserDTO user;
    private List<ApiCommentDTO> comments;

    private String imageFileName;

    // 이미지 DTO 리스트 추가
    private List<ApiFishingReportImageDTO> images;

    // 대표 썸네일도 따로 뽑아서 담기
    private String thumbnailUrl;

    // 기존 이미지 유지 플래그 추가
    private Boolean keepExistingImages;
    
    // 기존 이미지 URL 리스트 (수정 시 기존 이미지 정보 전달용)
    private List<String> existingImageUrls;

    public ApiFishingReportDTO(FishingReport fishingReport) {
        this.frId = fishingReport.getFrId();
        this.title = fishingReport.getTitle();
        this.content = fishingReport.getContent();
        this.fishingAt = LocalDate.from(fishingReport.getFishingAt());
        this.createdAt = fishingReport.getCreatedAt();

        this.imageFileName = fishingReport.getThumbnailUrl(); // 여기에 추가

        if (fishingReport.getProduct() != null) {
            this.product = new ApiProductDTO(fishingReport.getProduct());
            this.prodName = fishingReport.getProduct().getProdName();
        }

        if (fishingReport.getProduct() != null) {
            this.product = new ApiProductDTO(fishingReport.getProduct());
        }

        if (fishingReport.getUser() != null) {
            this.user = new ApiUserDTO(fishingReport.getUser());
        }

        if (fishingReport.getComments() != null) {
            this.comments = fishingReport.getComments().stream()
                    .map(ApiCommentDTO::new)
                    .collect(Collectors.toList());
        }

        // 이미지 리스트 매핑
        if (fishingReport.getImages() != null) {
            this.images = fishingReport.getImages().stream()
                    .map(ApiFishingReportImageDTO::new)
                    .collect(Collectors.toList());

            // 대표 썸네일 추출
            this.thumbnailUrl = fishingReport.getImages().stream()
                    .filter(FishingReportImage::isThumbnail)
                    .map(FishingReportImage::getImageUrl)
                    .findFirst()
                    .orElse(null);
        }
    }

    public static ApiFishingReportDTO fromEntity(FishingReport fishingReport) {
        return new ApiFishingReportDTO(fishingReport);
    }

    // 홈페이지용 생성자 (이미지 데이터 제외)
    public static ApiFishingReportDTO fromEntityForHome(FishingReport fishingReport) {
        ApiFishingReportDTO dto = new ApiFishingReportDTO();
        dto.frId = fishingReport.getFrId();
        dto.title = fishingReport.getTitle();
        dto.content = fishingReport.getContent();
        dto.fishingAt = LocalDate.from(fishingReport.getFishingAt());
        dto.createdAt = fishingReport.getCreatedAt();
        dto.imageFileName = fishingReport.getThumbnailUrl();

        if (fishingReport.getProduct() != null) {
            dto.product = new ApiProductDTO(fishingReport.getProduct());
            dto.prodName = fishingReport.getProduct().getProdName();
        }

        if (fishingReport.getUser() != null) {
            dto.user = new ApiUserDTO(fishingReport.getUser());
        }

        // 이미지 URL만 포함 (데이터는 제외)
        if (fishingReport.getImages() != null) {
            dto.images = fishingReport.getImages().stream()
                    .map(image -> {
                        ApiFishingReportImageDTO imageDto = new ApiFishingReportImageDTO();
                        imageDto.setImageUrl(image.getImageUrl());
                        imageDto.setThumbnail(image.isThumbnail());
                        // imageData와 thumbnailData는 설정하지 않음
                        return imageDto;
                    })
                    .collect(Collectors.toList());

            // 대표 썸네일 추출
            dto.thumbnailUrl = fishingReport.getImages().stream()
                    .filter(FishingReportImage::isThumbnail)
                    .map(FishingReportImage::getImageUrl)
                    .findFirst()
                    .orElse(null);
        }

        return dto;
    }

}

