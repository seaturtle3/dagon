package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentsVerifyResponseDTO {
    private boolean success;
    private String message;
}
