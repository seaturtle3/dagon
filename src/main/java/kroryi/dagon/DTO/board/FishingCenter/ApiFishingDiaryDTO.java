package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kroryi.dagon.entity.fishingCenter.FishingDiary;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiFishingDiaryDTO {
    private Long fdId;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime fishingAt;

    private ApiProductDTO product;
    private ApiUserDTO user;
    private List<ApiCommentDTO> comments;

    public ApiFishingDiaryDTO(FishingDiary fishingDiary) {
        this.fdId = fishingDiary.getFdId();
        this.title = fishingDiary.getTitle();
        this.content = fishingDiary.getContent();
        this.fishingAt = fishingDiary.getFishingAt();

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
    }

    public static ApiFishingDiaryDTO fromEntity(FishingDiary fishingDiary) {
        return new ApiFishingDiaryDTO(fishingDiary);
    }

}