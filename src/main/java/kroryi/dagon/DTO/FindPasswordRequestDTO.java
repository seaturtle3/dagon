package kroryi.dagon.DTO;

import lombok.Data;

@Data
public class FindPasswordRequestDTO {
    private String uid;
    private String uname;
    private String email;
}
