package kroryi.dagon.DTO.board;

import io.swagger.v3.oas.annotations.media.Schema;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.FishingReportComment;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ApiFishingReportDTO {
    private Long frId;
    private String title;
    private String content;

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

    public ApiFishingReportDTO(FishingReport fishingReport) {
        this.frId = fishingReport.getFrId();
        this.title = fishingReport.getTitle();
        this.content = fishingReport.getContent();

        this.user = fishingReport.getUser();
        this.product = fishingReport.getProduct();
        this.comments = fishingReport.getComments();
        this.prodName = fishingReport.getProduct().getProdName();  // ✅ 이렇게
        this.userName = fishingReport.getUser().getUname();  // ✅ 이렇게
    }

}

