package kroryi.dagon.DTO;

import jakarta.validation.constraints.NotNull;
import kroryi.dagon.enums.InquiryType;
import lombok.Data;

@Data
public class PartnerInquiryCreateRequestDTO {
    @NotNull
    private Long productId; // 상품 ID
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private InquiryType inquiryType;
} 