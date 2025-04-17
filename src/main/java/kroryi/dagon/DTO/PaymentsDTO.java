package kroryi.dagon.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentsDTO {
    private Long id;
    private String impUid;
    private String merchantUid;
    private BigDecimal amount;
    private String buyerName;
    private String status;
    private String payMethod;
    private LocalDateTime paidAt;
}
