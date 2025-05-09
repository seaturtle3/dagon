package kroryi.dagon.DTO.board.FishingReportDiary;

import kroryi.dagon.entity.FishingReport;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiFishingReportDTO {
    private Long frId;
    private String title;
    private String content;

    private ApiProductDTO product;
    private ApiUserDTO user;
    private List<ApiCommentDTO> comments;

    public ApiFishingReportDTO(FishingReport fishingReport) {
        this.frId = fishingReport.getFrId();
        this.title = fishingReport.getTitle();
        this.content = fishingReport.getContent();

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

    }

}

