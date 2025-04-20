package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.FishingReportComment;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class FishingReportDTO {
    private Long frId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime fishingAt;
    private LocalDateTime modifyAt;
    private int views;
    private User user;
    private Product product;
    private List<FishingReportComment> comments;

    public FishingReportDTO(FishingReport fishingReport) {
        this.frId = fishingReport.getFrId();
        this.title = fishingReport.getTitle();
        this.content = fishingReport.getContent();
        this.thumbnailUrl = fishingReport.getThumbnailUrl();
        this.fishingAt = fishingReport.getFishingAt();
        this.modifyAt = fishingReport.getModifyAt();
        this.views = fishingReport.getViews();
        this.user = fishingReport.getUser();
        this.product = fishingReport.getProduct();
        this.comments = fishingReport.getComments();
    }
}

