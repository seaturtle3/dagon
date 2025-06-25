package kroryi.dagon.DTO.board.FishingCenter;

import io.swagger.v3.oas.annotations.media.Schema;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.fishingCenter.FishingReportComment;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Schema(hidden = true)
    private User user;

    @Schema(hidden = true)
    private Product product;
    private List<FishingReportComment> comments;
    private String prodName;
    private String userName;

    public Long getProdId() {
        return product != null ? product.getProdId() : null;
    }

    public FishingReportDTO(FishingReport fishingReport) {
        this.frId = fishingReport.getFrId();
        this.title = fishingReport.getTitle();
        this.content = fishingReport.getContent();
        this.fishingAt = fishingReport.getFishingAt();
        this.modifyAt = fishingReport.getModifyAt();
        this.views = fishingReport.getViews();
        this.user = fishingReport.getUser();
        this.product = fishingReport.getProduct();
        this.comments = fishingReport.getComments();
        this.prodName = fishingReport.getProduct().getProdName();  // ✅ 이렇게
        this.userName = fishingReport.getUser().getUname();  // ✅ 이렇게
    }


}

