package kroryi.dagon.DTO;


import kroryi.dagon.entity.FishingReport;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FishingReportDTO {
    private Long frId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime fishingAt;

    public FishingReportDTO(FishingReport report) {
        this.frId = report.getFrId();
        this.title = report.getTitle();
        this.content = report.getContent();
        this.thumbnailUrl = report.getThumbnailUrl();
        this.fishingAt = report.getFishingAt();
    }
}


