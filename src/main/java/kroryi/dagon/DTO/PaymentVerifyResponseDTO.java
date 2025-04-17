package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class PaymentVerifyResponseDTO {
    private boolean success;
    private String message;
}
