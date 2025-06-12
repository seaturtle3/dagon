package kroryi.dagon.DTO;

import kroryi.dagon.enums.InquiryType;
import lombok.Data;

@Data
public class InquiryUpdateRequestDTO {
    private String title;
    private String content;
    private String writerType;
    private InquiryType inquiryType;
}
