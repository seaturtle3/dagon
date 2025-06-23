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
    private String imageFileName;


    private ApiProductDTO product;
    private ApiUserDTO user;
    private List<ApiCommentDTO> comments;

    // 이미지 DTO 리스트 추가
    private List<ApiFishingReportImageDTO> images;

    // 대표 썸네일도 따로 뽑아서 담기
    private String thumbnailUrl;

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

}

