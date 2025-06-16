package kroryi.dagon.DTO.board;


import kroryi.dagon.entity.fishingCenter.FishingReport;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PartnerFishingReportDTO {
    private Long frId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime fishingAt;
    private String prodName;

    public PartnerFishingReportDTO(FishingReport report) {
        this.frId = report.getFrId();
        this.title = report.getTitle();
        this.content = report.getContent();
        this.thumbnailUrl = report.getThumbnailUrl();
        this.fishingAt = report.getFishingAt();
        if (report.getProduct() != null) {
            this.prodName = report.getProduct().getProdName();
        } else {
            this.prodName = null;
        }
    }
}


