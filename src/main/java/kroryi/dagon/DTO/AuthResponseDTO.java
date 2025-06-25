package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import kroryi.dagon.entity.User;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String message;
}