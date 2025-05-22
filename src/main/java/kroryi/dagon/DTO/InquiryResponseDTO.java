package kroryi.dagon.DTO;

import kroryi.dagon.enums.InquiryType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InquiryResponseDTO {
    private Long id;
    private String title;
    private String content;
    private InquiryType inquiryType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userName;       // 작성자 이름
    private String userUid;        // 작성자 아이디
    private String partnerName;    // 문의 대상 파트너명
    private LocalDateTime answeredAt;  // 답변 완료 시간 (있으면)
    // 추가: 답변 내용
    private String answerContent;
}
