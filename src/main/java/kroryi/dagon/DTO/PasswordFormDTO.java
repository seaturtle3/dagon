package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordFormDTO {
    private String userId;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;


}
