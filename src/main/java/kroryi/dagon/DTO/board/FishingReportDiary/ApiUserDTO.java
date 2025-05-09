package kroryi.dagon.DTO.board.FishingReportDiary;

import kroryi.dagon.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiUserDTO {
    private Long uno;
    private String uid;
    private String uname;
    private String nickname;
    private String email;
    private String Phone;

    public ApiUserDTO(User user) {
        this.uno = user.getUno();
        this.uid = user.getUid();
        this.uname = user.getUname();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.Phone = user.getPhone();
    }
}
