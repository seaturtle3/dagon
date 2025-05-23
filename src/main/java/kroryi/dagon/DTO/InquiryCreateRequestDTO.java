package kroryi.dagon.DTO;

import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import lombok.Data;

@Data
public class InquiryCreateRequestDTO {
    private ReceiverType receiverType;
    private Long partnerId; // 문의 대상 파트너 ID
    private String title;
    private String content;
    private InquiryType inquiryType;
    private String partnerName;
}
