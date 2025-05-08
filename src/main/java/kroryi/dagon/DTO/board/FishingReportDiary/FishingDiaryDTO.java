package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class FishingDiaryDTO {
    private Long fdId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime fishingAt;
    private LocalDateTime modifyAt;
    private int views;
    private User user;
    private Product product;
    private List<FishingReportComment> comments;
    private String prodName;
    private String userName;

    public Long getProdId() {
        return product != null ? product.getProdId() : null;
    }

    public FishingDiaryDTO(FishingDiary fishingDiary) {
        this.fdId = fishingDiary.getFdId();
        this.title = fishingDiary.getTitle();
        this.content = fishingDiary.getContent();
        this.thumbnailUrl = fishingDiary.getThumbnailUrl();
        this.fishingAt = fishingDiary.getFishingAt();
        this.modifyAt = fishingDiary.getModifyAt();
        this.views = fishingDiary.getViews();
        this.user = fishingDiary.getUser();
        this.product = fishingDiary.getProduct();
//        this.comments = fishingDiary.getComments();
        this.prodName = fishingDiary.getProduct().getProdName();  // ✅ 이렇게
        this.userName = fishingDiary.getUser().getUname();  // ✅ 이렇게
    }


}

